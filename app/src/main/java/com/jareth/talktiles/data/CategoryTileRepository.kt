package com.jareth.talktiles.data

import kotlinx.coroutines.flow.Flow

class CategoryTileRepository(private val dao: CategoryTileDao) {
    fun getAll(): Flow<List<CategoryTile>> = dao.getAll()
}
