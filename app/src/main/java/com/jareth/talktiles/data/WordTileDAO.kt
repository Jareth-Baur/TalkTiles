// WordTileDao.kt
package com.jareth.talktiles.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WordTileDao {

    @Query("SELECT * FROM word_tile WHERE category = :category")
    fun getWordsByCategory(category: String): Flow<List<WordTile>>

    @Query("SELECT * FROM word_tile WHERE category = :category")
    fun getByCategory(category: String): Flow<List<WordTile>>

    @Query("SELECT * FROM word_tile")
    fun getAll(): Flow<List<WordTile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tile: WordTile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tiles: List<WordTile>) // ✅ ADDED FOR JSON BULK INSERT

    @Delete
    suspend fun delete(tile: WordTile)

    @Update
    suspend fun update(tile: WordTile)

    @Query("SELECT * FROM word_tile WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<WordTile>>

    @Update
    suspend fun updateWordTile(wordTile: WordTile)

    @Query("SELECT * FROM word_tile")
    fun getAllTiles(): Flow<List<WordTile>>

    @Update
    suspend fun updateTile(tile: WordTile)

}
