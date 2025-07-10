package com.jareth.talktiles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jareth.talktiles.data.WordTile
import com.jareth.talktiles.data.WordTileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


class WordTileViewModel constructor(
    private val repository: WordTileRepository
) : ViewModel() {

    fun getTilesByCategory(category: String): Flow<List<WordTile>> {
        return repository.getTilesByCategory(category)
    }

    fun insertDefaultTiles() {
        viewModelScope.launch {
            repository.insertDefaultTiles()
        }
    }
}
