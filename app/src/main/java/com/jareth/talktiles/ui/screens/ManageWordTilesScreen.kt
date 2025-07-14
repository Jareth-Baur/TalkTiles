
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.jareth.talktiles.data.WordTile
import com.jareth.talktiles.viewmodel.CategoryTileViewModel
import com.jareth.talktiles.viewmodel.WordTileViewModel
import kotlinx.coroutines.launch

@Composable
fun ManageWordTilesScreen(
    wordTileViewModel: WordTileViewModel,
    categoryTileViewModel: CategoryTileViewModel,
    onBack: () -> Unit
) {
    val allWords by wordTileViewModel.allTiles.collectAsState(initial = emptyList())
    val categories by categoryTileViewModel.allCategories.collectAsState(initial = emptyList())

    var showAddDialog by remember { mutableStateOf(false) }
    var selectedWord by remember { mutableStateOf<WordTile?>(null) }
    var sortOption by remember { mutableStateOf("A–Z") }
    var categoryFilter by remember { mutableStateOf("All Categories") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val sortOptions = listOf("A–Z", "Z–A")
    val categoryOptions = listOf("All Categories") + categories.map { it.label }

    val filteredWords = allWords
        .filter { categoryFilter == "All Categories" || it.category == categoryFilter }
        .sortedWith(
            if (sortOption == "A–Z") compareBy { it.label } else compareByDescending { it.label }
        )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Word", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text(
                "Manage Word Tiles",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SimpleDropdown(
                    label = "Sort by",
                    options = sortOptions,
                    selected = sortOption,
                    onSelected = { sortOption = it },
                    modifier = Modifier.weight(1f)
                )
                SimpleDropdown(
                    label = "Category",
                    options = categoryOptions,
                    selected = categoryFilter,
                    onSelected = { categoryFilter = it },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filteredWords, key = { it.id }) { word ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "${word.emoji} ${word.label} (${word.category})",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Row {
                                IconButton(onClick = { selectedWord = word }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                                }
                                IconButton(onClick = {
                                    selectedWord = word.copy(label = "") // Delete trigger
                                }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Add Dialog
            if (showAddDialog) {
                var label by remember { mutableStateOf(TextFieldValue("")) }
                var emoji by remember { mutableStateOf(TextFieldValue("")) }
                var selectedCategory by remember { mutableStateOf(categories.firstOrNull()?.label ?: "") }

                AlertDialog(
                    onDismissRequest = { showAddDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            if (label.text.isNotBlank() && emoji.text.isNotBlank()) {
                                wordTileViewModel.insert(
                                    WordTile(
                                        emoji = emoji.text,
                                        label = label.text,
                                        category = selectedCategory
                                    )
                                )
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Word tile added")
                                }
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
                            Spacer(Modifier.height(8.dp))
                            Text("Select Category", style = MaterialTheme.typography.bodyMedium)

                            SimpleDropdown(
                                label = "Category",
                                options = categories.map { it.label },
                                selected = selectedCategory,
                                onSelected = { selectedCategory = it }
                            )
                        }
                    }
                )
            }

            // Edit/Delete Dialog
            selectedWord?.let { word ->
                if (word.label.isEmpty()) {
                    AlertDialog(
                        onDismissRequest = { selectedWord = null },
                        confirmButton = {
                            TextButton(onClick = {
                                wordTileViewModel.delete(word)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Word tile deleted")
                                }
                                selectedWord = null
                            }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
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
                    var editedCategory by remember { mutableStateOf(word.category) }

                    AlertDialog(
                        onDismissRequest = { selectedWord = null },
                        confirmButton = {
                            TextButton(onClick = {
                                wordTileViewModel.update(
                                    word.copy(
                                        label = editedLabel.text,
                                        emoji = editedEmoji.text,
                                        category = editedCategory
                                    )
                                )
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Word tile updated")
                                }
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
                                Spacer(Modifier.height(8.dp))
                                Text("Edit Category", style = MaterialTheme.typography.bodyMedium)

                                SimpleDropdown(
                                    label = "Category",
                                    options = categories.map { it.label },
                                    selected = editedCategory,
                                    onSelected = { editedCategory = it }
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleDropdown(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}