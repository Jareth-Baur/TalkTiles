// WordTile.kt
package com.jareth.talktiles.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_tile")
data class WordTile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val emoji: String,
    val label: String,
    val category: String,
    val isFavorite: Boolean = false // ← NEW FIELD
)
