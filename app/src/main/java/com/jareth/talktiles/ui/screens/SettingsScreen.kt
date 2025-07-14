package com.jareth.talktiles.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    isParentModeEnabled: Boolean,
    onToggleParentMode: (Boolean) -> Unit,
    onNavigateTo: (String) -> Unit
) {
    var showPinDialog by remember { mutableStateOf(false) }
    var showDisableConfirmationDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        SettingsCategory(title = "General")

        SettingsTile(
            title = "Parent Mode",
            icon = Icons.Default.AdminPanelSettings,
            subtitle = if (isParentModeEnabled) "Enabled" else "Disabled",
            onClick = {
                if (!isParentModeEnabled) {
                    showPinDialog = true
                } else {
                    //onToggleParentMode(false)
                    showDisableConfirmationDialog = true
                }
            }
        )

        if (isParentModeEnabled) {
            SettingsCategory(title = "Features")

            SettingsTile(
                title = "Voice Settings",
                icon = Icons.Default.RecordVoiceOver,
                onClick = { onNavigateTo("voice") }
            )

            SettingsTile(
                title = "Language",
                icon = Icons.Default.Language,
                onClick = { onNavigateTo("language") }
            )

            SettingsTile(
                title = "Data Management",
                icon = Icons.Default.Storage,
                onClick = { onNavigateTo("data") }
            )

            SettingsCategory(title = "App Info")

            SettingsTile(
                title = "About",
                icon = Icons.Default.Info,
                onClick = { onNavigateTo("about") }
            )
        } else {
            Text(
                text = "Enable Parent Mode to access more settings.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }

    if (showPinDialog) {
        ParentPinDialog(
            onPinEntered = { success ->
                if (success) onToggleParentMode(true)
                showPinDialog = false
            },
            onDismiss = { showPinDialog = false }
        )
    }

    if (showDisableConfirmationDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDisableConfirmationDialog = false },
            title = { Text("Disable Parent Mode") },
            text = { Text("Are you sure you want to turn off Parent Mode?") },
            confirmButton = {
                Text(
                    "Yes",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            onToggleParentMode(false)
                            showDisableConfirmationDialog = false
                        },
                    color = MaterialTheme.colorScheme.primary
                )
            },
            dismissButton = {
                Text(
                    "Cancel",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            showDisableConfirmationDialog = false
                        },
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        )
    }
}


@Composable
fun SettingsTile(
    title: String,
    icon: ImageVector,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val elevation by animateDpAsState(
        targetValue = if (isPressed) 10.dp else 4.dp,
        label = "TileElevation"
    )

    val backgroundColor = if (isPressed) Color(0xFFF0F0F0) else Color(0xFFFAFAFA)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        tonalElevation = 2.dp,
        shadowElevation = elevation,
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF222222),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
                )
                subtitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsCategory(title: String) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelSmall.copy(
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.padding(top = 16.dp, start = 4.dp)
    )
}
