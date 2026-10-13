package com.example.comunicados

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = onBackToLogin,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "Volver",
                        tint = Color(0xFF2D3748)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = painterResource(id = R.drawable.cue_logo),
                contentDescription = "Logo Cue",
                modifier = Modifier
                    .width(200.dp)
                    .height(140.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))


            Text(
                text = "Recupera tu contraseña de manera sencilla",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D3B5E),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))


            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text("Correo electrónico", color = Color(0xFF9E9E9E), fontSize = 15.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = null,
                        tint = Color(0xFF757575)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF35A7E8),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121)
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (email.isBlank()) {
                        errorMessage = "Ingresa tu correo electrónico."
                        successMessage = ""
                    } else {
                        // correo de recuperación con Firebase //
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEE8BA3)
                )
            ) {
                Text(
                    text = "Recuperar contraseña",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Mensaje de error//
            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onBackToLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE1F5FE)
                )
            ) {
                Text(
                    text = "Volver al Login",
                    color = Color(0xFF35A7E8),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(90.dp))
        }

        // Ondas decorativas //
        BottomWavesDecoration(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp)
                .align(Alignment.BottomCenter)
        )

        if (successMessage.isNotEmpty()) {
            AlertDialog(
                onDismissRequest = {
                    successMessage = ""
                    onBackToLogin()
                },
                containerColor = Color(0xFFE1F5FE),
                title = {
                    Text(
                        text = "Correo enviado",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D3B5E),
                        fontSize = 18.sp
                    )
                },
                text = {
                    Text(
                        text = "Se ha enviado un correo para restablecer tu contraseña.",
                        color = Color(0xFF1D3B5E),
                        fontSize = 15.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            successMessage = ""
                            onBackToLogin()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEE8BA3)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Aceptar", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                },
                shape = RoundedCornerShape(24.dp)
            )
        }
    }
}


@Composable
private fun BottomWavesDecoration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        
        val pinkPath = Path().apply {
            moveTo(0f, height)
            lineTo(0f, height * 0.35f)
            cubicTo(
                width * 0.25f, height * 0.15f,
                width * 0.5f, height * 0.65f,
                width * 0.85f, height * 0.85f
            )
            lineTo(width, height)
            close()
        }
        drawPath(
            path = pinkPath,
            color = Color(0xFFFAD1DC)
        )

        val bluePath = Path().apply {
            moveTo(width, height)
            lineTo(width, height * 0.30f)
            cubicTo(
                width * 0.75f, height * 0.10f,
                width * 0.55f, height * 0.70f,
                width * 0.25f, height * 0.88f
            )
            lineTo(0f, height)
            close()
        }
        drawPath(
            path = bluePath,
            color = Color(0xFFD4EFFC).copy(alpha = 0.85f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecoverPasswordScreenPreview() {
    ComunicadosTheme {
        RecoverPasswordScreen(onBackToLogin = {})
    }
}
