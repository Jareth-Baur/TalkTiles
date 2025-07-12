package com.jareth.talktiles.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryTileDao {
    @Query("SELECT * FROM category_tile")
    fun getAll(): Flow<List<CategoryTile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryTile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryTile>) // ✅ ADDED FOR JSON BULK INSERT

    @Query("SELECT * FROM category_tile") // ✅ MUST MATCH @Entity(tableName = ...)
    fun getAllCategories(): Flow<List<CategoryTile>>

    @Delete
    suspend fun delete(tile: CategoryTile) // ← ✅ ADD THIS

    @Update
    suspend fun update(tile: CategoryTile) // ← ✅ Add this line
}
