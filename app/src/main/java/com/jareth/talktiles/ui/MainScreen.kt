package com.jareth.talktiles.ui

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jareth.talktiles.data.AppDatabase
import com.jareth.talktiles.data.CategoryTile
import com.jareth.talktiles.data.WordTileRepository
import com.jareth.talktiles.ui.components.CategoryTileCard
import com.jareth.talktiles.ui.components.WordTileCard
import com.jareth.talktiles.ui.screens.ManageCategoriesScreen
import com.jareth.talktiles.ui.screens.ManageWordTilesScreen
import com.jareth.talktiles.ui.screens.SettingsScreen
import com.jareth.talktiles.viewmodel.CategoryTileViewModel
import com.jareth.talktiles.viewmodel.CategoryTileViewModelFactory
import com.jareth.talktiles.viewmodel.WordTileViewModel
import com.jareth.talktiles.viewmodel.WordTileViewModelFactory
import java.util.Locale

@Composable
fun MainScreen() {
    val context = LocalContext.current

    // Text-to-Speech setup
    val ttsRef = remember { mutableStateOf<TextToSpeech?>(null) }
    LaunchedEffect(Unit) {
        ttsRef.value = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsRef.value?.language = Locale.US
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            ttsRef.value?.stop()
            ttsRef.value?.shutdown()
        }
    }

    // ViewModels
    val categoryViewModel: CategoryTileViewModel = viewModel(
        factory = CategoryTileViewModelFactory(context.applicationContext)
    )
    val db = AppDatabase.getInstance(context)
    val wordTileRepository = WordTileRepository(db.wordTileDao())
    val wordViewModel: WordTileViewModel = viewModel(
        factory = WordTileViewModelFactory(wordTileRepository)
    )

    // UI states
    var selectedCategory by rememberSaveable { mutableStateOf<CategoryTile?>(null) }
    var sentence by rememberSaveable { mutableStateOf(listOf<String>()) }
    var showFavorites by rememberSaveable { mutableStateOf(false) }

    // Data
    val allWords by wordViewModel.allTiles.collectAsState(initial = emptyList())
    val favoriteWords = allWords.filter { it.isFavorite }
    val categories by categoryViewModel.allCategories.collectAsState(initial = emptyList())

    // ✅ Persistent scroll state for home category grid
    val categoryGridState = rememberSaveable(saver = LazyGridState.Saver) {
        LazyGridState()
    }
    val favoritesGridState = rememberSaveable(saver = LazyGridState.Saver) {
        LazyGridState()
    }

    var inSettings by rememberSaveable { mutableStateOf(false) }

    var isParentModeEnabled by rememberSaveable { mutableStateOf(false) }

    var currentScreen by rememberSaveable { mutableStateOf("main") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Sentence bar
        if (currentScreen != "settings" && currentScreen != "manageWords" && currentScreen != "manageCategories") {
        SentenceBar(
                words = sentence,
                onSpeak = {
                    val text = sentence.joinToString(" ")
                    ttsRef.value?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                },
                onClear = { sentence = emptyList() },
                onDeleteLast = {
                    if (sentence.isNotEmpty()) sentence = sentence.dropLast(1)
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Main content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (currentScreen) {
                "settings" -> {
                    SettingsScreen(
                        wordTileViewModel = wordViewModel,
                        categoryTileViewModel = categoryViewModel,
                        isParentModeEnabled = isParentModeEnabled,
                        onToggleParentMode = { isParentModeEnabled = it },
                        onManageWords = { currentScreen = "manageWords" },
                        onManageCategories = { currentScreen = "manageCategories" }
                    )
                }

                "manageWords" -> {
                    ManageWordTilesScreen(
                        wordTileViewModel = wordViewModel,
                        onBack = { currentScreen = "settings" }
                    )
                }

                "manageCategories" -> {
                    ManageCategoriesScreen(
                        categoryTileViewModel = categoryViewModel,
                        onBack = { currentScreen = "settings" }
                    )
                }

                else -> {
                    when {
                        showFavorites -> {
                            LazyVerticalGrid(
                                state = favoritesGridState,
                                columns = GridCells.Fixed(3),
                                contentPadding = PaddingValues(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(favoriteWords, key = { it.id }) { word ->
                                    WordTileCard(
                                        emoji = word.emoji,
                                        label = word.label,
                                        isFavorite = word.isFavorite,
                                        onClick = { sentence = sentence + word.label },
                                        onToggleFavorite = {
                                            wordViewModel.toggleFavorite(word)
                                            Log.d("FavoriteToggle", "Toggled favorite for: ${word.label}")
                                        }
                                    )
                                }
                            }
                        }

                        selectedCategory == null -> {
                            LazyVerticalGrid(
                                state = categoryGridState,
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(categories, key = { it.id }) { category ->
                                    CategoryTileCard(
                                        emoji = category.emoji,
                                        label = category.label,
                                        onClick = { selectedCategory = category }
                                    )
                                }
                            }
                        }

                        else -> {
                            val words by wordViewModel
                                .getTilesByCategory(selectedCategory!!.label)
                                .collectAsState(initial = emptyList())
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                contentPadding = PaddingValues(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(words, key = { it.id }) { word ->
                                    WordTileCard(
                                        emoji = word.emoji,
                                        label = word.label,
                                        isFavorite = word.isFavorite,
                                        onClick = { sentence = sentence + word.label },
                                        onToggleFavorite = {
                                            wordViewModel.toggleFavorite(word)
                                            Log.d("FavoriteToggle", "Toggled favorite for: ${word.label}")
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }

        // Bottom Navigation Bar
        BottomNavigationBar(
            isInWordView = selectedCategory != null,
            onBack = {
                selectedCategory = null
                inSettings = false
            },
            onShowFavorites = {
                selectedCategory = null
                showFavorites = true
                inSettings = false
            },
            onShowHome = {
                selectedCategory = null
                showFavorites = false
                inSettings = false
            },
            onShowSettings = {
                selectedCategory = null
                showFavorites = false
                currentScreen = "settings"
            }
        )
    }
}

@Composable
fun SentenceBar(
    words: List<String>,
    onSpeak: () -> Unit,
    onDeleteLast: () -> Unit,
    onClear: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // more padding for space
        ) {
            // Sentence preview
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 12.dp), // increased bottom space
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (words.isEmpty()) {
                    Text(
                        text = "Tap tiles to build a sentence",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
                    )
                } else {
                    Text(
                        text = words.joinToString(" "),
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
                    )
                }
            }

            // Buttons row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                KidActionButton(
                    emoji = "🧹",
                    label = "Clear",
                    color = Color(0xFFFFF59D),
                    onClick = onClear,
                    enabled = words.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                )
                KidActionButton(
                    emoji = "🗑",
                    label = "Undo",
                    color = Color(0xFFFFCCBC),
                    onClick = onDeleteLast,
                    enabled = words.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                )
                KidActionButton(
                    emoji = "🔊",
                    label = "Speak",
                    color = Color(0xFFB2EBF2),
                    onClick = onSpeak,
                    enabled = words.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun KidActionButton(
    emoji: String,
    label: String,
    color: Color,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp) // <– tiny space between emoji and label
        ) {
            Text(text = emoji, fontSize = 30.sp) // slightly bigger
            Text(text = label, fontSize = 13.sp)
        }
    }
}

@Composable
fun BottomNavigationBar(
    isInWordView: Boolean,
    onBack: () -> Unit,
    onShowFavorites: () -> Unit, // ✅ add this line
    onShowHome: () -> Unit, // ✅ add this
    onShowSettings: () -> Unit // <-- add this
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isInWordView) {
            KidNavButton(
                emoji = "⬅️",
                label = "Back",
                color = Color(0xFFB39DDB),
                onClick = { onBack() },
                modifier = Modifier.weight(1f)
            )
        } else {
            KidNavButton(
                emoji = "🏠",
                label = "Home",
                color = Color(0xFF90CAF9),
                onClick = onShowHome, // ✅ uses passed function
                modifier = Modifier.weight(1f)
            )
        }

        KidNavButton(
            emoji = "❤️",
            label = "Faves",
            color = Color(0xFFFFCDD2),
            onClick = { onShowFavorites() },
            modifier = Modifier.weight(1f)
        )

        KidNavButton(
            emoji = "⚙️",
            label = "Settings",
            color = Color(0xFFA5D6A7),
            onClick = onShowSettings,
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
fun KidNavButton(
    emoji: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = emoji, fontSize = 28.sp)
            Text(text = label, fontSize = 12.sp)
        }
    }
}
