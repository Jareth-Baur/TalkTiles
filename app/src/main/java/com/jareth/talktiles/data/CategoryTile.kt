package com.jareth.talktiles.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_tile")
data class CategoryTile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val emoji: String,
    val label: String
)
