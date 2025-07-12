
package com.jareth.talktiles.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.jareth.talktiles.data.WordTile
import com.jareth.talktiles.viewmodel.WordTileViewModel

@Composable
fun ManageWordTilesScreen(
    wordTileViewModel: WordTileViewModel,
    onBack: () -> Unit
) {
    val wordTiles by wordTileViewModel.allTiles.collectAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedWord by remember { mutableStateOf<WordTile?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Word")
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text("Manage Word Tiles", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(wordTiles, key = { it.id }) { word ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${word.emoji} ${word.label}")
                            Row {
                                IconButton(onClick = { selectedWord = word }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                                }
                                IconButton(onClick = {
                                    selectedWord = word.copy(label = "") // Trigger delete confirmation
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }

            if (showAddDialog) {
                var label by remember { mutableStateOf(TextFieldValue("")) }
                var emoji by remember { mutableStateOf(TextFieldValue("")) }

                AlertDialog(
                    onDismissRequest = { showAddDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            if (label.text.isNotBlank() && emoji.text.isNotBlank()) {
                                wordTileViewModel.insert(WordTile(emoji = emoji.text, label = label.text))  // this is causing the error because no category
                            }
                            showAddDialog = false
                        }) { Text("Add") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
                    },
                    title = { Text("Add New Word Tile") },
                    text = {
                        Column {
                            OutlinedTextField(value = emoji, onValueChange = { emoji = it }, label = { Text("Emoji") })
                            OutlinedTextField(value = label, onValueChange = { label = it }, label = { Text("Label") })
                        }
                    }
                )
            }

            selectedWord?.let { word ->
                if (word.label.isEmpty()) {
                    // Delete confirmation
                    AlertDialog(
                        onDismissRequest = { selectedWord = null },
                        confirmButton = {
                            TextButton(onClick = {
                                wordTileViewModel.delete(word)
                                selectedWord = null
                            }) { Text("Delete", color = Color.Red) }
                        },
                        dismissButton = {
                            TextButton(onClick = { selectedWord = null }) { Text("Cancel") }
                        },
                        title = { Text("Delete Word Tile?") },
                        text = { Text("Are you sure you want to delete this word tile?") }
                    )
                } else {
                    var editedLabel by remember { mutableStateOf(TextFieldValue(word.label)) }
                    var editedEmoji by remember { mutableStateOf(TextFieldValue(word.emoji)) }

                    AlertDialog(
                        onDismissRequest = { selectedWord = null },
                        confirmButton = {
                            TextButton(onClick = {
                                wordTileViewModel.update(word.copy(label = editedLabel.text, emoji = editedEmoji.text))
                                selectedWord = null
                            }) { Text("Save") }
                        },
                        dismissButton = {
                            TextButton(onClick = { selectedWord = null }) { Text("Cancel") }
                        },
                        title = { Text("Edit Word Tile") },
                        text = {
                            Column {
                                OutlinedTextField(value = editedEmoji, onValueChange = { editedEmoji = it }, label = { Text("Emoji") })
                                OutlinedTextField(value = editedLabel, onValueChange = { editedLabel = it }, label = { Text("Label") })
                            }
                        }
                    )
                }
            }
        }
    }
}
