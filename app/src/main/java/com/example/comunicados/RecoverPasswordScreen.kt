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
fun RecoverPasswordScreen(onBackToLogin: () -> Unit) {
    // Estado para el campo de correo //
    var email by remember { mutableStateOf("") }
    // Estado para mostrar el mensaje de éxito
    var showMessage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título  pantalla //
        Text(
            text = "Recuperar contraseña",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo de Correo electrónico //
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de Recuperar contraseña //
        Button(
            onClick = { showMessage = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Recuperar contraseña")
        }

        // Mensaje de confirmación //
        if (showMessage) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Se ha solicitado la recuperación para: $email",
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Vínculo para volver al Login//
        TextButton(onClick = onBackToLogin) {
            Text("Volver al Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecoverPasswordScreenPreview() {
    ComunicadosTheme {
        RecoverPasswordScreen(onBackToLogin = {})
    }
}
