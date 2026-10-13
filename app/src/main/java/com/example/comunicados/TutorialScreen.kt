package com.example.comunicados

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comunicados.ui.theme.ComunicadosTheme

@Composable
fun TutorialScreen(onBack: () -> Unit) {
    val darkBackground = Color(0xFF101014)
    val cardBackground = Color(0xFF1C1C22)
    val cardBorder = Color(0xFF2E2E38)
    val pastelLila = Color(0xFFE2D4F8)
    val pastelRosa = Color(0xFFFFC5D3)
    val pastelCeleste = Color(0xFFBEE3F8)
    val darkText = Color(0xFF18181C)
    val mutedGray = Color(0xFFA0A0B2)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
    ) {
        TopLeftWave(
            modifier = Modifier
                .width(140.dp)
                .height(80.dp)
                .align(Alignment.TopStart)
        )
        TopRightWave(
            modifier = Modifier
                .width(140.dp)
                .height(80.dp)
                .align(Alignment.TopEnd)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 52.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "¿Cómo usar Cue :) ?",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = pastelLila
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Guía rápida para conocer las funciones principales.",
                fontSize = 14.sp,
                color = mutedGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(pastelRosa, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ChatBubbleOutline,
                                contentDescription = null,
                                tint = darkText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "1. Escribir",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Permite redactar mensajes personalizados y utilizar frases rápidas para comunicarte fácilmente en distintas situaciones.",
                        fontSize = 14.sp,
                        color = Color(0xFFD0D0E0),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(pastelLila, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Mic,
                                contentDescription = null,
                                tint = darkText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "2. Hablar",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Permite escribir mensajes para reproducirlos en voz alta y utilizar las opciones de síntesis de voz disponibles.",
                        fontSize = 14.sp,
                        color = Color(0xFFD0D0E0),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(pastelCeleste, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Smartphone,
                                contentDescription = null,
                                tint = darkText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "3. Buscar dispositivo",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Permite consultar la ubicación actual del dispositivo y administrar los registros guardados.",
                        fontSize = 14.sp,
                        color = Color(0xFFD0D0E0),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Home,
                                contentDescription = null,
                                tint = darkText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "4. Volver al inicio",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Puedes regresar al menú principal en cualquier momento presionando el botón 'Volver al Home' para seleccionar otra función.",
                        fontSize = 14.sp,
                        color = Color(0xFFD0D0E0),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = pastelCeleste)
            ) {
                Text(
                    text = "Volver al Home",
                    color = darkText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(90.dp))
        }

        BottomHomeWavesDecoration(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun TopLeftWave(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val bluePath = Path().apply {
            moveTo(0f, 0f)
            lineTo(w, 0f)
            cubicTo(w * 0.7f, h * 0.9f, w * 0.3f, h * 0.8f, 0f, h * 0.6f)
            close()
        }
        drawPath(bluePath, color = Color(0xFFD4EFFC))

        val pinkPath = Path().apply {
            moveTo(0f, 0f)
            lineTo(w * 0.85f, 0f)
            cubicTo(w * 0.5f, h * 0.7f, w * 0.2f, h * 0.5f, 0f, h * 0.4f)
            close()
        }
        drawPath(pinkPath, color = Color(0xFFFAD1DC))
    }
}

@Composable
private fun TopRightWave(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val pinkPath = Path().apply {
            moveTo(w, 0f)
            lineTo(0f, 0f)
            cubicTo(w * 0.3f, h * 0.9f, w * 0.7f, h * 0.8f, w, h * 0.6f)
            close()
        }
        drawPath(pinkPath, color = Color(0xFFE2D4F8))

        val bluePath = Path().apply {
            moveTo(w, 0f)
            lineTo(w * 0.15f, 0f)
            cubicTo(w * 0.5f, h * 0.7f, w * 0.8f, h * 0.5f, w, h * 0.4f)
            close()
        }
        drawPath(bluePath, color = Color(0xFFD4EFFC))
    }
}

@Composable
private fun BottomHomeWavesDecoration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val lilaPath = Path().apply {
            moveTo(0f, h)
            lineTo(0f, h * 0.4f)
            cubicTo(w * 0.3f, h * 0.1f, w * 0.7f, h * 0.7f, w, h * 0.3f)
            lineTo(w, h)
            close()
        }
        drawPath(lilaPath, color = Color(0xFFE2D4F8))

        val pinkPath = Path().apply {
            moveTo(0f, h)
            lineTo(0f, h * 0.6f)
            cubicTo(w * 0.25f, h * 0.3f, w * 0.65f, h * 0.8f, w, h * 0.5f)
            lineTo(w, h)
            close()
        }
        drawPath(pinkPath, color = Color(0xFFFAD1DC).copy(alpha = 0.9f))

        val bluePath = Path().apply {
            moveTo(w, h)
            lineTo(w, h * 0.45f)
            cubicTo(w * 0.75f, h * 0.2f, w * 0.35f, h * 0.85f, 0f, h * 0.7f)
            lineTo(0f, h)
            close()
        }
        drawPath(bluePath, color = Color(0xFFD4EFFC).copy(alpha = 0.85f))
    }
}

@Preview(showBackground = true)
@Composable
fun TutorialScreenPreview() {
    ComunicadosTheme {
        TutorialScreen(onBack = {})
    }
}
