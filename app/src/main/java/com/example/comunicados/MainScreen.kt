package com.example.comunicados

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.PanTool
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    userName: String,
    onWriteClick: () -> Unit,
    onSpeakClick: () -> Unit,
    onSearchDeviceClick: () -> Unit,
    onTutorialClick: (() -> Unit)? = null
) {

    var useSignLanguage by remember { mutableStateOf(false) }
    var useTextToSpeech by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var communicationType by remember { mutableStateOf("Selecciona tipo") }

    val typeOptions = listOf("Formal", "Informal", "Emergencia")

    // Paleta de colores para la pantalla Home //
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
        // Ondas superiore //
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
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // titulo //
            Text(
                text = "Cue :)",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = pastelLila,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Una forma más simple\nde comunicarnos.",
                fontSize = 15.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))


            Text(
                text = "¡Bienvenid@, ${userName.nombreFormateado()}! 👋",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = pastelLila,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            //  Contexto de la comunicación //
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
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Contexto de la comunicación:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Tipo",
                        fontSize = 14.sp,
                        color = mutedGray
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Selector desplegable //
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        Box(
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .height(48.dp)
                                .background(Color(0xFF14141A), RoundedCornerShape(12.dp))
                                .border(1.dp, cardBorder, RoundedCornerShape(12.dp))
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = communicationType,
                                    color = if (communicationType == "Selecciona tipo") mutedGray else Color.White,
                                    fontSize = 15.sp
                                )
                                Icon(
                                    imageVector = Icons.Outlined.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = mutedGray
                                )
                            }
                        }

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(Color(0xFF22222B))
                        ) {
                            typeOptions.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption, color = Color.White) },
                                    onClick = {
                                        communicationType = selectionOption
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ¿coomo quieres comunicarte? //
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¿Cómo quieres comunicarte?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(120.dp)
                                .background(pastelRosa, RoundedCornerShape(20.dp))
                                .clickable { onWriteClick() }
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Outlined.ChatBubbleOutline,
                                    contentDescription = null,
                                    tint = darkText,
                                    modifier = Modifier.size(42.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Escribir",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = darkText
                                )
                            }
                        }

                        // Botón Hablar //
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(120.dp)
                                .background(pastelLila, RoundedCornerShape(20.dp))
                                .clickable { onSpeakClick() }
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Outlined.Mic,
                                    contentDescription = null,
                                    tint = darkText,
                                    modifier = Modifier.size(42.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Hablar",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = darkText
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Buscar dispositivo //
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .background(pastelCeleste, RoundedCornerShape(20.dp))
                            .clickable { onSearchDeviceClick() }
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Smartphone,
                                contentDescription = null,
                                tint = darkText,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Buscar dispositivo",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = darkText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .background(pastelLila, RoundedCornerShape(20.dp))
                            .clickable { onTutorialClick?.invoke() }
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.HelpOutline,
                                contentDescription = null,
                                tint = darkText,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Tutorial de uso",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = darkText
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Información de formas de comunicación //
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
                                imageVector = Icons.Outlined.MenuBook,
                                contentDescription = null,
                                tint = darkText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Información de formas de comunicación:",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Tabla informativa //
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF14141A), RoundedCornerShape(14.dp))
                            .border(1.dp, cardBorder, RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Forma de comunicación",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = mutedGray,
                                    modifier = Modifier.weight(1.1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Descripción",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = mutedGray,
                                    modifier = Modifier.weight(1.4f)
                                )
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 10.dp),
                                color = cardBorder
                            )

                            // Escribir //
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1.1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .background(pastelRosa, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.ChatBubbleOutline,
                                            contentDescription = null,
                                            tint = darkText,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Escribir",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Permite redactar mensajes de texto.",
                                    fontSize = 13.sp,
                                    color = Color(0xFFD0D0E0),
                                    modifier = Modifier.weight(1.4f)
                                )
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 10.dp),
                                color = cardBorder
                            )

                            // Hablar //
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1.1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .background(pastelLila, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Mic,
                                            contentDescription = null,
                                            tint = darkText,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Hablar",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Permite convertir texto en voz y comunicarse verbalmente.",
                                    fontSize = 13.sp,
                                    color = Color(0xFFD0D0E0),
                                    modifier = Modifier.weight(1.4f)
                                )
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 10.dp),
                                color = cardBorder
                            )

                            //Buscar dispositivo //
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1.1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .background(pastelCeleste, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Smartphone,
                                            contentDescription = null,
                                            tint = darkText,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Buscar dispositivo",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Permite acceder a la función Buscar dispositivo.",
                                    fontSize = 13.sp,
                                    color = Color(0xFFD0D0E0),
                                    modifier = Modifier.weight(1.4f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ¿Qué ayuda necesitas? //
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = pastelRosa)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = darkText,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "¿Qué ayuda necesitas?",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = darkText
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Selecciona las opciones que te resulten más cómodas.",
                        fontSize = 13.sp,
                        color = Color(0xFF4A4A58)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Opción Lengua de señas //
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .background(Color(0xFFF2D5EA), RoundedCornerShape(14.dp))
                                .clickable { useSignLanguage = !useSignLanguage }
                                .padding(horizontal = 8.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = useSignLanguage,
                                    onCheckedChange = { useSignLanguage = it },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = darkText,
                                        uncheckedColor = Color(0xFF888898)
                                    )
                                )
                                Icon(
                                    imageVector = Icons.Outlined.PanTool,
                                    contentDescription = null,
                                    tint = darkText,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Lengua de señas",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = darkText
                                )
                            }
                        }

                        // Opción Texto a voz //
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .background(Color(0xFFD0ECFA), RoundedCornerShape(14.dp))
                                .clickable { useTextToSpeech = !useTextToSpeech }
                                .padding(horizontal = 8.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = useTextToSpeech,
                                    onCheckedChange = { useTextToSpeech = it },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = darkText,
                                        uncheckedColor = Color(0xFF888898)
                                    )
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                    contentDescription = null,
                                    tint = darkText,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Texto a voz",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = darkText
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(90.dp))
        }

        // Ondas decorativas inferiores //
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
fun MainScreenPreview() {
    ComunicadosTheme {
        MainScreen(
            userName = "Scarlett",
            onWriteClick = {},
            onSpeakClick = {},
            onSearchDeviceClick = {}
        )
    }
}
