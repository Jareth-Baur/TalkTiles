package com.jareth.talktiles.ui.screens

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jareth.talktiles.ui.components.WordTileCard
import com.jareth.talktiles.viewmodel.WordTileViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WordGridScreen(
    category: String,
    viewModel: WordTileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val tts = remember {
        TextToSpeech(context) { status ->
            if (status != TextToSpeech.SUCCESS) {
                Toast.makeText(context, "TTS failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val words by viewModel.getTilesByCategory(category).collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Category: $category", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(words) { word ->
                WordTileCard(word) {
                    tts.speak(word.label, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        }
    }
}
