// WordTileRepository.kt
package com.jareth.talktiles.data

import kotlinx.coroutines.flow.Flow

class WordTileRepository(private val dao: WordTileDao) {
    fun getTilesByCategory(category: String): Flow<List<WordTile>> {
        return dao.getWordsByCategory(category) // ✅ make sure this exists in DAO
    }

    fun getAllWordTiles(): Flow<List<WordTile>> {
        return dao.getAllTiles()
    }

    fun getFavorites(): Flow<List<WordTile>> = dao.getFavorites()

    suspend fun updateWordTile(tile: WordTile) = dao.updateWordTile(tile)

    fun getAll(): Flow<List<WordTile>> = dao.getAll()

    fun getByCategory(category: String): Flow<List<WordTile>> = dao.getByCategory(category)

    suspend fun insert(tile: WordTile) = dao.insert(tile)

    suspend fun delete(tile: WordTile) = dao.delete(tile)

    suspend fun update(tile: WordTile) = dao.update(tile)
}