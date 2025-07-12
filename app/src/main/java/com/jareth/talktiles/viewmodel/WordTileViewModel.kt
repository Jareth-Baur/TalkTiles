package com.jareth.talktiles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jareth.talktiles.data.WordTile
import com.jareth.talktiles.data.WordTileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WordTileViewModel(private val repository: WordTileRepository) : ViewModel() {

    val favorites: Flow<List<WordTile>> = repository.getFavorites()

    val allTiles: Flow<List<WordTile>> = repository.getAllWordTiles()

    fun toggleFavorite(tile: WordTile) {
        val updatedTile = tile.copy(isFavorite = !tile.isFavorite)
        viewModelScope.launch {
            repository.updateWordTile(updatedTile)
        }
    }


    fun getTilesByCategory(categoryLabel: String): Flow<List<WordTile>> {
        return repository.getByCategory(categoryLabel)
    }

    fun insert(wordTile: WordTile) {
        viewModelScope.launch {
            repository.insert(wordTile)
        }
    }

    fun update(wordTile: WordTile) {
        viewModelScope.launch {
            repository.update(wordTile)
        }
    }

    fun delete(wordTile: WordTile) {
        viewModelScope.launch {
            repository.delete(wordTile)
        }
    }

}
