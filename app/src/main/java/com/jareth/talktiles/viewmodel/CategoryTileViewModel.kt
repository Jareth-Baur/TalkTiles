package com.jareth.talktiles.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jareth.talktiles.data.AppDatabase
import com.jareth.talktiles.data.CategoryTile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CategoryTileViewModel(context: Context) : ViewModel() {
    private val dao = AppDatabase.getInstance(context).categoryTileDao()
    val allCategories: Flow<List<CategoryTile>> = dao.getAll()

    fun insert(tile: CategoryTile) = viewModelScope.launch {
        dao.insert(tile)
    }

    fun delete(tile: CategoryTile) = viewModelScope.launch {
        dao.delete(tile)
    }

    fun update(tile: CategoryTile) = viewModelScope.launch {
        dao.update(tile)
    }
    fun deleteAll() {
        viewModelScope.launch {
            dao.deleteAllCategoryTiles()
        }
    }

}

