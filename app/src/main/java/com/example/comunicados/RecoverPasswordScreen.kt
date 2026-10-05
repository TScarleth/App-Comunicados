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
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RecoverPasswordScreen(onBackToLogin: () -> Unit) {
    // Estado para el campo de correo //
    var email by remember { mutableStateOf("") }
    // Estados para mensajes de éxito y error //
    var successMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Instancia de Firebase Auth //
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título de la pantalla //
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
            onClick = {
                if (email.isBlank()) {
                    errorMessage = "Ingresa tu correo electrónico."
                    successMessage = ""
                } else {
                    // Enviar correo de recuperación con Firebase //
                    auth.sendPasswordResetEmail(email.trim())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                errorMessage = ""
                                successMessage = "Se ha enviado un correo para restablecer tu contraseña."
                            } else {
                                successMessage = ""
                                errorMessage = "No se pudo enviar el correo de recuperación. Verifica el correo ingresado."
                            }
                        }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Recuperar contraseña")
        }

        // Mensaje de éxito //
        if (successMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = successMessage,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        // Mensaje de error //
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Vínculo para volver al Login //
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
