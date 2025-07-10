package com.jareth.talktiles.data

fun sampleWordTiles(): List<WordTile> = listOf(
    WordTile(id = "1", label = "Eat", imageRes = android.R.drawable.ic_menu_crop, category = "Food"),
    WordTile(id = "2", label = "Drink", imageRes = android.R.drawable.ic_menu_gallery, category = "Food"),
    WordTile(id = "3", label = "Mom", imageRes = android.R.drawable.ic_menu_call, category = "People"),
    WordTile(id = "4", label = "Dad", imageRes = android.R.drawable.ic_menu_call, category = "People"),
    WordTile(id = "5", label = "Play", imageRes = android.R.drawable.ic_media_play, category = "Activity"),
    WordTile(id = "6", label = "Sleep", imageRes = android.R.drawable.ic_lock_idle_alarm, category = "Activity")
)
