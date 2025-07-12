package com.jareth.talktiles.ui.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun ParentPinDialog(
    onPinEntered: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    val correctPin = "1234" // 🔒 Default PIN (you can move this to a secure setting later)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Enter Parent PIN") },
        text = {
            OutlinedTextField(
                value = pin,
                onValueChange = {
                    if (it.length <= 4) pin = it
                },
                label = { Text("PIN") },
                visualTransformation = PasswordVisualTransformation()
            )
        },
        confirmButton = {
            Button(onClick = {
                onPinEntered(pin == correctPin)
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
