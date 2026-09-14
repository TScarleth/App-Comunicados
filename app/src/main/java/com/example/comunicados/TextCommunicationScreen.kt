package com.example.comunicados

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.comunicados.ui.theme.ComunicadosTheme

@Composable
fun TextCommunicationScreen(onBack: () -> Unit) {
    // Estados para la comunicación por texto //
    var message by remember { mutableStateOf("") }
    var voiceType by remember { mutableStateOf("Masculina") }

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
            onClick = { /* Acción básica */ },
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
