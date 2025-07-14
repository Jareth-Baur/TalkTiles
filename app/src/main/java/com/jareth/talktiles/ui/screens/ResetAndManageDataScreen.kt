package com.jareth.talktiles.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jareth.talktiles.ui.components.TopBackArrow
import kotlinx.coroutines.launch

@Composable
fun ResetAndManageDataScreen(
    onBack: () -> Unit,
    onResetFavorites: () -> Unit,
    onResetAll: () -> Unit,
    onManageWords: () -> Unit,
    onManageCategories: () -> Unit
) {
    var showConfirmResetAll by remember { mutableStateOf(false) }
    var showConfirmFavorites by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 16.dp),
                snackbar = { snackbarData ->
                    Snackbar(
                        snackbarData = snackbarData,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        ) {

            TopBackArrow(
                onClick = onBack,
                useEmoji = true,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 72.dp, start = 16.dp, end = 16.dp)
            ) {
                Text(
                    "Manage & Reset Data",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text("Manage Tiles", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(12.dp))

                RoundedButton("Manage Word Tiles", onClick = onManageWords)
                Spacer(modifier = Modifier.height(8.dp))
                RoundedButton("Manage Categories", onClick = onManageCategories)

                Spacer(modifier = Modifier.height(32.dp))

                Text("Reset Data", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(12.dp))

                RoundedButton(
                    text = "Reset Favorites",
                    onClick = { showConfirmFavorites = true },
                    color = Color(0xFFD32F2F)
                )

                Spacer(modifier = Modifier.height(8.dp))

                RoundedButton(
                    text = "Reset All Data",
                    onClick = { showConfirmResetAll = true },
                    color = Color(0xFFD32F2F)
                )
            }
        }

        if (showConfirmFavorites) {
            ConfirmResetDialog(
                title = "Reset Favorites?",
                message = "This will remove all favorite tiles.",
                onConfirm = {
                    onResetFavorites()
                    showConfirmFavorites = false
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Favorites reset successfully")
                    }
                },
                onDismiss = { showConfirmFavorites = false }
            )
        }

        if (showConfirmResetAll) {
            ConfirmResetDialog(
                title = "Reset All Data?",
                message = "This will delete all categories and word tiles. Continue?",
                onConfirm = {
                    onResetAll()
                    showConfirmResetAll = false
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("All data deleted successfully")
                    }
                },
                onDismiss = { showConfirmResetAll = false }
            )
        }
    }
}

@Composable
fun RoundedButton(
    text: String,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White
        )
    ) {
        Text(text)
    }
}
