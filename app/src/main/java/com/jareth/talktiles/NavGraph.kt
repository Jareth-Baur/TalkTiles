package com.jareth.talktiles

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jareth.talktiles.ui.screens.HomeScreen
import com.jareth.talktiles.ui.screens.WordGridScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(onCategoryClick = { category ->
                navController.navigate("words/$category")
            })
        }
        composable("words/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            WordGridScreen(category)
        }
    }
}
