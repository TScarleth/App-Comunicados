package com.example.comunicados

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.comunicados.ui.theme.ComunicadosTheme
import java.util.Locale

@Composable
fun TextCommunicationScreen(onBack: () -> Unit) {
    // Estados para la comunicación por texto //
    var message by remember { mutableStateOf("") }
    var voiceType by remember { mutableStateOf("Masculina") }

    // Configurar texto a voz //
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(context) {
        lateinit var textToSpeech: TextToSpeech
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.forLanguageTag("es-ES")
            }
        }
        tts = textToSpeech

        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la pantalla //
        Text(
            text = "Comunicación por texto",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Escribe tu mensaje y elige la voz que prefieras.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Preferencia de voz //
        Text("Preferencia de voz:", fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = voiceType == "Masculina",
                onClick = { voiceType = "Masculina" }
            )
            Text("Masculina")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = voiceType == "Femenina",
                onClick = { voiceType = "Femenina" }
            )
            Text("Femenina")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Campo para escribir mensaje //
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Mensaje para convertir") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de voz //
        Button(
            onClick = {
                if (message.isNotBlank() && tts != null) {
                    // Seleccionar voz según preferencia //
                    val targetGender = if (voiceType == "Masculina") "male" else "female"
                    val matchedVoice = tts?.voices?.find { voice ->
                        voice.name.lowercase().contains(targetGender)
                    }
                    if (matchedVoice != null) {
                        tts?.voice = matchedVoice
                        tts?.setPitch(1.0f)
                    } else {
                        tts?.setPitch(if (voiceType == "Masculina") 0.8f else 1.2f)
                    }
                    // Reproducir el mensaje //
                    tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Convertir texto en voz")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón para volver //
        TextButton(onClick = onBack) {
            Text("Volver")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextCommunicationScreenPreview() {
    ComunicadosTheme {
        TextCommunicationScreen(onBack = {})
    }
}
