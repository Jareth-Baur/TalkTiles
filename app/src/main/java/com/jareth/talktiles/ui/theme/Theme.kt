package com.jareth.talktiles.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2F2F2F),       // ← soft blue (your custom)
    onPrimary = Color.White,           // text/icon color on primary
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFF5F5F5),
    onSurface = Color(0xFF1C1B1F),
)


@Composable
fun TalkTilesTheme(
    darkTheme: Boolean = false,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme // ⬅️ skip the when{} entirely if not needed

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}