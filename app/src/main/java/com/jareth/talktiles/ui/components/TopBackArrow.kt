package com.jareth.talktiles.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBackArrow(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    useEmoji: Boolean = false,
    tint: Color = Color.Black
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.CenterStart
    ) {
        if (useEmoji) {
            Text(
                text = "⬅️",
                fontSize = 28.sp,
                color = tint
            )
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = tint
            )
        }
    }
}
