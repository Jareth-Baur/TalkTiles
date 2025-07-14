package com.jareth.talktiles.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jareth.talktiles.ui.components.TopBackArrow

@Composable
fun AboutScreen(onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {

        // 🔙 Floating emoji or icon back arrow
        TopBackArrow(
            onClick = onBack,
            useEmoji = true,
            modifier = Modifier
                .align(Alignment.TopStart).statusBarsPadding()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 72.dp, start = 16.dp, end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "About Talk Tiles",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Talk Tiles is a customizable Augmentative and Alternative Communication (AAC) app designed to support children with communication challenges. It allows users to build sentences using tiles, promoting self-expression, independence, and language development.",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Version: 1.0.0",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Developer: Jareth Baur",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Support: jarethbaur0223@gmail.com",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
