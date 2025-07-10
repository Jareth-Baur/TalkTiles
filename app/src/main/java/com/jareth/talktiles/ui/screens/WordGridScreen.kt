package com.jareth.talktiles.ui.screens

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jareth.talktiles.data.TalkTilesDatabase
import com.jareth.talktiles.data.WordTile
import com.jareth.talktiles.data.WordTileRepository
import com.jareth.talktiles.ui.components.WordTileCard

@Composable
fun WordGridScreen(category: String) {
    val context = LocalContext.current
    val db = remember { TalkTilesDatabase.getDatabase(context) }
    val dao = remember { db.wordTileDao() }
    val repo = remember { WordTileRepository(dao) }

    val tts = remember {
        TextToSpeech(context) { status ->
            if (status != TextToSpeech.SUCCESS) {
                Toast.makeText(context, "TTS failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    var wordTiles by remember { mutableStateOf<List<WordTile>>(emptyList()) }

    LaunchedEffect(category) {
        // Insert default tiles if not already present
        repo.insertDefaultTiles()
        wordTiles = repo.getTilesByCategory(category)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Category: $category", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(wordTiles) { word ->
                WordTileCard(word) {
                    tts.speak(word.label, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        }
    }
}

