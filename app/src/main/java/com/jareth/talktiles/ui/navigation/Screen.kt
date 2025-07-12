package com.jareth.talktiles.ui.navigation

import com.jareth.talktiles.data.CategoryTile

sealed class Screen {
    object Categories : Screen()
    data class Words(val category: CategoryTile) : Screen()
    object Favorites : Screen()
    object Settings : Screen()
}
