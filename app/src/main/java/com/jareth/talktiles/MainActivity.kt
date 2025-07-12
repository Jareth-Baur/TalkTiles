package com.jareth.talktiles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jareth.talktiles.ui.MainScreen
import com.jareth.talktiles.ui.theme.TalkTilesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TalkTilesTheme {
                MainScreen() // Main logic is in MainScreen.kt
            }
        }
    }
}