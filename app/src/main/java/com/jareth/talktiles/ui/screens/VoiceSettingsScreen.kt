package com.jareth.talktiles.ui.screens

import TTSManager
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jareth.talktiles.ui.components.TopBackArrow
import java.util.Locale

@Composable
fun VoiceSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var pitch by remember { mutableFloatStateOf(1.0f) }
    var rate by remember { mutableFloatStateOf(1.0f) }
    var voices by remember { mutableStateOf<List<Voice>>(emptyList()) }
    var selectedVoice by remember { mutableStateOf<Voice?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var genderFilter by remember { mutableStateOf("Any") }
    var localeFilter by remember { mutableStateOf("All") }

    LaunchedEffect(Unit) {
        TTSManager.init(context) {
            tts = TTSManager.getTTS()

            val allVoices = tts?.voices?.filter { it.locale.language == "en" } ?: emptySet()
            voices = allVoices.sortedBy { it.name }.toList()

            selectedVoice = TTSManager.getSavedVoice(context, allVoices) ?: tts?.voice
            pitch = TTSManager.getSavedPitch(context)
            rate = TTSManager.getSavedSpeechRate(context)
        }
    }

    val filteredVoices = voices.filter { voice ->
        val gender = inferGender(voice)
        val genderMatch = (genderFilter == "Any" || gender == genderFilter)
        val localeName = "${voice.locale.getDisplayLanguage(Locale.getDefault())} (${voice.locale.country})"
        val localeMatch = (localeFilter == "All" || localeFilter == localeName)
        genderMatch && localeMatch
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                .padding(top = 72.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Text("Voice Settings", style = MaterialTheme.typography.headlineSmall)

            Spacer(Modifier.height(16.dp))

            Text("Pitch: %.1f".format(pitch))
            Slider(
                value = pitch,
                onValueChange = {
                    pitch = it
                    TTSManager.setPitch(context, it)
                },
                valueRange = 0.5f..2.0f
            )

            Spacer(Modifier.height(16.dp))

            Text("Speech Rate: %.1f".format(rate))
            Slider(
                value = rate,
                onValueChange = {
                    rate = it
                    TTSManager.setSpeechRate(context, it)
                },
                valueRange = 0.5f..2.0f
            )

            Spacer(Modifier.height(24.dp))

            // Filters Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Gender")
                    DropdownMenuBox(
                        current = genderFilter,
                        options = listOf("Any", "Male", "Female"),
                        onSelect = { genderFilter = it }
                    )
                }

                val localeOptions = listOf("All") + voices
                    .map { "${it.locale.getDisplayLanguage(Locale.getDefault())} (${it.locale.country})" }
                    .distinct()

                Column(modifier = Modifier.weight(1f)) {
                    Text("Accent")
                    DropdownMenuBox(
                        current = localeFilter,
                        options = localeOptions,
                        onSelect = { localeFilter = it }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            if (filteredVoices.isNotEmpty()) {
                Text("Choose Voice", fontSize = 16.sp)

                Box {
                    OutlinedButton(onClick = { dropdownExpanded = true }) {
                        Text(selectedVoice?.let {
                            "${inferGender(it)} - ${it.locale.displayLanguage} (${it.locale.country})"
                        } ?: "Select Voice")
                    }

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        filteredVoices.forEach { voice ->
                            val label =
                                "${inferGender(voice)} - ${voice.locale.displayLanguage} (${voice.locale.country})"

                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    selectedVoice = voice
                                    dropdownExpanded = false
                                    TTSManager.setVoice(context, voice)
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
            }

            Button(onClick = {
                TTSManager.getTTS()?.apply {
                    setPitch(pitch)
                    setSpeechRate(rate)
                    selectedVoice?.let { voice = it }
                    speak("Hello, this is a sample voice", TextToSpeech.QUEUE_FLUSH, null, "test_voice")
                }
            }) {
                Text("Test Voice")
            }
        }
    }
}

@Composable
fun DropdownMenuBox(current: String, options: List<String>, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(current)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun inferGender(voice: Voice): String {
    val lowerName = voice.name.lowercase()
    val features = voice.features?.map { it.lowercase() } ?: emptySet()

    return when {
        "female" in lowerName || "female" in features -> "Female"
        "male" in lowerName || "male" in features -> "Male"
        else -> "Neutral"
    }
}
