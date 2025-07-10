package com.jareth.talktiles.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_tiles")
data class WordTile(
    @PrimaryKey val id: String,
    val label: String,
    val imageRes: Int, // You can change this to URI later for custom images
    val category: String
)
