package com.jareth.talktiles.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WordTileDao {
    @Query("SELECT * FROM word_tiles WHERE category = :category")
    suspend fun getTilesByCategory(category: String): List<WordTile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tiles: List<WordTile>)
}
