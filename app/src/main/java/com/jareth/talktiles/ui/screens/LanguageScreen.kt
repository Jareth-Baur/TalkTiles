package com.jareth.talktiles.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jareth.talktiles.ui.components.TopBackArrow

@Composable
fun LanguageScreen(onBack: () -> Unit) {
    val languages = listOf("English", "Filipino", "Cebuano")
    var selected by remember { mutableStateOf("English") }

    Box(Modifier.fillMaxSize()) {
        // 🔙 Floating back arrow top-left (emoji style)
        TopBackArrow(
            onClick = onBack,
            useEmoji = true, // Change to false if you prefer the icon
            modifier = Modifier
                .align(Alignment.TopStart).statusBarsPadding()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 72.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = "Language Settings",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            languages.forEach { lang ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selected = lang }
                        .padding(vertical = 12.dp)
                ) {
                    RadioButton(
                        selected = selected == lang,
                        onClick = { selected = lang }
                    )
                    Text(text = lang)
                }
            }
        }
    }
}
