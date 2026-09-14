package com.example.comunicados

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.comunicados.ui.theme.ComunicadosTheme

@Composable
fun RegisterScreen(
    onUserRegistered: (User) -> Unit,
    errorMessage: String,
    successMessage: String,
    onBackToLogin: () -> Unit
) {
    // Estados para los campos de texto //
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    // Estado para mostrar/ocultar contraseña //
    var passwordVisible by remember { mutableStateOf(false) }
    // Estado para errores de validación local //
    var validationError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título de la pantalla //
        Text(
            text = "Registro de usuario",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo de Nombre //
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de correo electrónico //
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña //
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón  d registrarse //
        // Solo se muestra si no se ha registrado con éxito //
        if (successMessage.isEmpty()) {
            Button(
                onClick = {
                    // Validar campos vacíos //
                    if (name.isEmpty()) {
                        validationError = "El campo nombre es obligatorio."
                    } else if (email.isEmpty()) {
                        validationError = "El campo correo electrónico es obligatorio."
                    } else if (password.isEmpty()) {
                        validationError = "El campo contraseña es obligatorio."
                    } else {
                        // Intentar registrar al usuario //
                        try {
                            val newUser = User(name, email, password)
                            validationError = "" // Limpiar error previo //
                            onUserRegistered(newUser)
                        } catch (e: Exception) {
                            validationError = "Ocurrió un problema al registrar el usuario."
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }
        }

        // Mensaje de error de validación o del sistema //
        if (validationError.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = validationError, color = MaterialTheme.colorScheme.error)
        }

        // Mensaje de éxito //
        if (successMessage.isNotEmpty()) {
            Text(text = successMessage, color = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.height(16.dp))
            // Botón volver al Login //
            Button(
                onClick = onBackToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al Login")
            }
        }

        // Mensaje de error (lmite de usuarios) //
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    ComunicadosTheme {
        RegisterScreen(
            onUserRegistered = {},
            errorMessage = "",
            successMessage = "",
            onBackToLogin = {}
        )
    }
}
