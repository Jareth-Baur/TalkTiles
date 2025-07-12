
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
import com.jareth.talktiles.data.CategoryTile
import com.jareth.talktiles.viewmodel.CategoryTileViewModel

@Composable
fun ManageCategoriesScreen(
    categoryTileViewModel: CategoryTileViewModel,
    onBack: () -> Unit
) {
    val categories by categoryTileViewModel.allCategories.collectAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<CategoryTile?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Category")
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text("Manage Categories", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(categories, key = { it.id }) { category ->
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
                            Text("${category.emoji} ${category.label}")
                            Row {
                                IconButton(onClick = { selectedCategory = category }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                                }
                                IconButton(onClick = {
                                    selectedCategory = category.copy(label = "") // Trigger delete
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
                                categoryTileViewModel.insert(CategoryTile(emoji = emoji.text, label = label.text))
                            }
                            showAddDialog = false
                        }) { Text("Add") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
                    },
                    title = { Text("Add New Category") },
                    text = {
                        Column {
                            OutlinedTextField(value = emoji, onValueChange = { emoji = it }, label = { Text("Emoji") })
                            OutlinedTextField(value = label, onValueChange = { label = it }, label = { Text("Label") })
                        }
                    }
                )
            }

            selectedCategory?.let { category ->
                if (category.label.isEmpty()) {
                    // Delete confirmation
                    AlertDialog(
                        onDismissRequest = { selectedCategory = null },
                        confirmButton = {
                            TextButton(onClick = {
                                categoryTileViewModel.delete(category)
                                selectedCategory = null
                            }) { Text("Delete", color = Color.Red) }
                        },
                        dismissButton = {
                            TextButton(onClick = { selectedCategory = null }) { Text("Cancel") }
                        },
                        title = { Text("Delete Category?") },
                        text = { Text("Are you sure you want to delete this category?") }
                    )
                } else {
                    var editedLabel by remember { mutableStateOf(TextFieldValue(category.label)) }
                    var editedEmoji by remember { mutableStateOf(TextFieldValue(category.emoji)) }

                    AlertDialog(
                        onDismissRequest = { selectedCategory = null },
                        confirmButton = {
                            TextButton(onClick = {
                                categoryTileViewModel.update(category.copy(label = editedLabel.text, emoji = editedEmoji.text))
                                selectedCategory = null
                            }) { Text("Save") }
                        },
                        dismissButton = {
                            TextButton(onClick = { selectedCategory = null }) { Text("Cancel") }
                        },
                        title = { Text("Edit Category") },
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
