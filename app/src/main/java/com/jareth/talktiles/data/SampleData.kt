package com.jareth.talktiles.data

val sampleCategories = listOf(
CategoryTile(emoji = "🍽", label = "Food"),
CategoryTile(emoji = "🧍", label = "People"),
CategoryTile(emoji = "🏃", label = "Actions"),
CategoryTile(emoji = "😀", label = "Emotions"),
CategoryTile(emoji = "📍", label = "Places"),
CategoryTile(emoji = "⭐", label = "Favorites"),
)

fun sampleWordTiles(): List<WordTile> = listOf(
    // Food
    WordTile(emoji = "🍕", label = "Pizza", category = "Food"),
    WordTile(emoji = "🍎", label = "Apple", category = "Food"),
    WordTile(emoji = "🍞", label = "Bread", category = "Food"),
    WordTile(emoji = "🍔", label = "Burger", category = "Food"),
    WordTile(emoji = "🥛", label = "Milk", category = "Food"),
    WordTile(emoji = "🍟", label = "Fries", category = "Food"),

    // People
    WordTile(emoji = "👩", label = "Woman", category = "People"),
    WordTile(emoji = "👨", label = "Man", category = "People"),
    WordTile(emoji = "🧒", label = "Child", category = "People"),
    WordTile(emoji = "👵", label = "Grandma", category = "People"),
    WordTile(emoji = "👴", label = "Grandpa", category = "People"),

    // Actions
    WordTile(emoji = "🏃", label = "Run", category = "Actions"),
    WordTile(emoji = "🛏", label = "Sleep", category = "Actions"),
    WordTile(emoji = "🧼", label = "Wash", category = "Actions"),
    WordTile(emoji = "🗣️", label = "Talk", category = "Actions"),
    WordTile(emoji = "🪥", label = "Brush", category = "Actions"),

    // Emotions
    WordTile(emoji = "😀", label = "Happy", category = "Emotions"),
    WordTile(emoji = "😢", label = "Sad", category = "Emotions"),
    WordTile(emoji = "😠", label = "Angry", category = "Emotions"),
    WordTile(emoji = "😨", label = "Scared", category = "Emotions"),
    WordTile(emoji = "🥰", label = "Loved", category = "Emotions"),

    // Places
    WordTile(emoji = "🏠", label = "Home", category = "Places"),
    WordTile(emoji = "🏫", label = "School", category = "Places"),
    WordTile(emoji = "🏥", label = "Hospital", category = "Places"),
    WordTile(emoji = "🛒", label = "Store", category = "Places"),
    WordTile(emoji = "🌳", label = "Park", category = "Places"),

    // Favorites (can be from any category)
    WordTile(emoji = "❤️", label = "Love", category = "Favorites"),
    WordTile(emoji = "💡", label = "Yes", category = "Favorites"),
    WordTile(emoji = "❌", label = "No", category = "Favorites"),
    WordTile(emoji = "🙂", label = "Okay", category = "Favorites"),
    WordTile(emoji = "🙋", label = "Help", category = "Favorites")
)
