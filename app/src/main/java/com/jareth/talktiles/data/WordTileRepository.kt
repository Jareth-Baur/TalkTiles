package com.jareth.talktiles.data

class WordTileRepository(private val dao: WordTileDao) {
    suspend fun getTilesByCategory(category: String) = dao.getTilesByCategory(category)
    suspend fun insertDefaultTiles() = dao.insertAll(sampleWordTiles())
}
