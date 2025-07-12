package com.jareth.talktiles.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jareth.talktiles.ui.components.SettingsCard
import com.jareth.talktiles.viewmodel.CategoryTileViewModel
import com.jareth.talktiles.viewmodel.WordTileViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    wordTileViewModel: WordTileViewModel,
    categoryTileViewModel: CategoryTileViewModel,
    isParentModeEnabled: Boolean,
    onToggleParentMode: (Boolean) -> Unit,
    onManageWords: () -> Unit,
    onManageCategories: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedLanguage by rememberSaveable { mutableStateOf("English") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Screen title
        item {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        // Parent Mode (always visible)
        item {
            SettingsCard {
                IconSectionHeader(
                    icon = Icons.Default.AdminPanelSettings,
                    title = "Parent Mode",
                    iconColor = Color(0xFF000000)
                )
                ParentModeSettingSection(
                    isParentModeEnabled = isParentModeEnabled,
                    onToggleParentMode = onToggleParentMode
                )
            }
        }

        if (isParentModeEnabled) {
            // Voice Settings
            item {
                SettingsCard {
                    IconSectionHeader(
                        icon = Icons.Default.RecordVoiceOver,
                        title = "Voice Settings",
                        iconColor = Color(0xFF000000)
                    )
                    VoiceSettingsSection()
                }
            }

            // Language / Locale
            item {
                SettingsCard {
                    IconSectionHeader(
                        icon = Icons.Default.Language,
                        title = "Language",
                        iconColor = Color(0xFF000000)
                    )
                    LanguageSettingSection(
                        selectedLanguage = selectedLanguage,
                        onLanguageSelected = { selectedLanguage = it }
                    )
                }
            }

            // About Section
            item {
                SettingsCard {
                    IconSectionHeader(
                        icon = Icons.Default.Info,
                        title = "About",
                        iconColor = Color(0xFF000000)
                    )
                    AboutSection()
                }
            }

            // Reset Data Section
            item {
                SettingsCard {
                    IconSectionHeader(
                        icon = Icons.Default.Restore,
                        title = "Reset Data",
                        iconColor = Color(0xFF000000)
                    )
                    ResetDataSection(
                        onResetFavorites = {
                            coroutineScope.launch {
                                wordTileViewModel.resetFavorites()
                            }
                        },
                        onResetAll = {
                            coroutineScope.launch {
                                wordTileViewModel.deleteAll()
                                categoryTileViewModel.deleteAll()
                            }
                        }
                    )
                }
            }

            item {
                IconSectionHeader(icon = Icons.Default.Build, title = "Manage Tiles")
                TileManagementSection(
                    onManageWords = onManageWords,
                    onManageCategories = onManageCategories
                )
            }

        } else {
            // Message shown when Parent Mode is off
            item {
                Text(
                    text = "Enable Parent Mode to access full settings.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}

@Composable
fun VoiceSettingsSection() {
    val context = LocalContext.current
    val tts = remember { TextToSpeech(context, null) }

    var pitch by remember { mutableFloatStateOf(1.0f) }
    var rate by remember { mutableFloatStateOf(1.0f) }
    var testPhrase = "Hello, this is Talk Tiles!"


    Text(
        text = "Voice Settings",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )

    Spacer(modifier = Modifier.height(8.dp))
    Text("Pitch: %.1f".format(pitch))
    Slider(
        value = pitch,
        onValueChange = { pitch = it },
        valueRange = 0.5f..2.0f,
    )

    Spacer(modifier = Modifier.height(8.dp))
    Text("Speech Rate: %.1f".format(rate))
    Slider(
        value = rate,
        onValueChange = { rate = it },
        valueRange = 0.5f..2.0f,
    )

    Spacer(modifier = Modifier.height(8.dp))
    Button(
        onClick = {
            tts.setPitch(pitch)
            tts.setSpeechRate(rate)
            tts.speak(testPhrase, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    ) {
        Text("Test Voice")
    }
}

@Composable
fun AboutSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "About Talk Tiles",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Talk Tiles is a customizable assistive communication app designed to help children with special needs express themselves using simple word and picture tiles.",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "• Easy-to-use tile-based interface\n" +
                    "• Emoji support and text-to-speech (TTS)\n" +
                    "• Favorites and categories for quick access\n" +
                    "• Parent Mode for safe settings access",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Version: 1.0.0",
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = "Developer: Jareth Baur",
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = "Support: jarethbaur0223@gmail.com",
            style = MaterialTheme.typography.bodySmall
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSettingSection(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    val languages = listOf("English", "Filipino", "Cebuano")
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Language / Locale",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedLanguage,
                onValueChange = {},
                readOnly = true,
                label = { Text("Choose Language") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                languages.forEach { language ->
                    DropdownMenuItem(
                        text = { Text(language) },
                        onClick = {
                            onLanguageSelected(language)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ParentModeSettingSection(
    isParentModeEnabled: Boolean,
    onToggleParentMode: (Boolean) -> Unit
) {
    var showPinDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Parent Mode",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Enable Parent Mode")
            Switch(
                checked = isParentModeEnabled,
                onCheckedChange = {
                    if (!isParentModeEnabled) {
                        showPinDialog = true
                    } else {
                        onToggleParentMode(false)
                    }
                }
            )
        }
    }

    if (showPinDialog) {
        ParentPinDialog(
            onPinEntered = { success ->
                if (success) {
                    onToggleParentMode(true)
                }
                showPinDialog = false
            },
            onDismiss = { showPinDialog = false }
        )
    }
}

@Composable
fun ResetDataSection(
    onResetFavorites: () -> Unit,
    onResetAll: () -> Unit
) {
    var showDialogForFavorites by remember { mutableStateOf(false) }
    var showDialogForAll by remember { mutableStateOf(false) }
    val dangerColor = Color(0xFFD32F2F)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Reset Options",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Button(
            onClick = { showDialogForFavorites = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = dangerColor,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reset Favorites")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { showDialogForAll = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = dangerColor,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.DeleteForever,
                contentDescription = "Delete All",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reset All Data")
        }
    }

    if (showDialogForFavorites) {
        ConfirmResetDialog(
            title = "Reset Favorites?",
            message = "This will remove all favorites. This cannot be undone.",
            onConfirm = {
                onResetFavorites()
                showDialogForFavorites = false
            },
            onDismiss = { showDialogForFavorites = false }
        )
    }

    if (showDialogForAll) {
        ConfirmResetDialog(
            title = "Reset All Data?",
            message = "This will delete all word and category tiles. Continue?",
            onConfirm = {
                onResetAll()
                showDialogForAll = false
            },
            onDismiss = { showDialogForAll = false }
        )
    }
}

@Composable
fun IconSectionHeader(
    icon: ImageVector,
    title: String,
    iconColor: Color = Color(0xFFB2EBF2) // Optional: light cyan
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun TileManagementSection(
    onManageWords: () -> Unit,
    onManageCategories: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {

        Text(
            text = "Manage Tiles",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Button(
            onClick = onManageWords,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Manage Word Tiles")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onManageCategories,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Manage Categories")
        }
    }
}

