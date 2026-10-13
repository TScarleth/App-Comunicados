package com.example.comunicados

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comunicados.ui.theme.ComunicadosTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

data class SpeakPhrase(
    val id: String = "",
    val texto: String = ""
)

@Composable
fun SpeakScreen(onBack: () -> Unit) {
    var phraseText by remember { mutableStateOf("") }
    var voiceType by remember { mutableStateOf("Masculina") }
    var statusMessage by remember { mutableStateOf("") }
    var editingPhraseId by remember { mutableStateOf<String?>(null) }
    var savedPhrases by remember { mutableStateOf<List<SpeakPhrase>>(emptyList()) }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val database = FirebaseDatabase.getInstance()

    DisposableEffect(currentUser?.uid) {
        val uid = currentUser?.uid
        if (uid != null) {
            val phrasesRef = database.getReference("frases").child(uid)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<SpeakPhrase>()
                    for (child in snapshot.children) {
                        val id = child.child("id").getValue(String::class.java) ?: child.key ?: ""
                        val texto = child.child("texto").getValue(String::class.java) ?: ""
                        if (id.isNotEmpty()) {
                            list.add(SpeakPhrase(id, texto))
                        }
                    }
                    savedPhrases = list
                }

                override fun onCancelled(error: DatabaseError) {
                    statusMessage = "No se pudieron cargar las frases."
                }
            }
            phrasesRef.addValueEventListener(listener)
            onDispose {
                phrasesRef.removeEventListener(listener)
            }
        } else {
            onDispose { }
        }
    }

    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(context) {
        var textToSpeech: TextToSpeech? = null
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale.forLanguageTag("es-ES"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    textToSpeech?.language = Locale.getDefault()
                }
            }
        }
        tts = textToSpeech

        onDispose {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
    }

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
                    text = "Hablar",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = pastelLila
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Escribe una frase para reproducirla en voz alta o guardarla.",
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
                    Text(
                        text = "Preferencia de voz:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = voiceType == "Masculina",
                            onClick = { voiceType = "Masculina" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = pastelLila,
                                unselectedColor = mutedGray
                            )
                        )
                        Text("Masculina", color = Color.White, fontSize = 15.sp)

                        Spacer(modifier = Modifier.width(20.dp))

                        RadioButton(
                            selected = voiceType == "Femenina",
                            onClick = { voiceType = "Femenina" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = pastelRosa,
                                unselectedColor = mutedGray
                            )
                        )
                        Text("Femenina", color = Color.White, fontSize = 15.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    OutlinedTextField(
                        value = phraseText,
                        onValueChange = { phraseText = it },
                        placeholder = {
                            Text("Frase para reproducir...", color = mutedGray, fontSize = 15.sp)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF14141A),
                            unfocusedContainerColor = Color(0xFF14141A),
                            focusedBorderColor = pastelCeleste,
                            unfocusedBorderColor = cardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            if (phraseText.isNotBlank() && tts != null) {
                                try {
                                    if (voiceType == "Masculina") {
                                        tts?.setPitch(0.65f)
                                    } else {
                                        tts?.setPitch(1.2f)
                                    }
                                    tts?.speak(phraseText, TextToSpeech.QUEUE_FLUSH, null, "UtteranceId")
                                } catch (_: Exception) {
                                    statusMessage = "No se pudo reproducir la frase."
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = pastelLila)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                            contentDescription = null,
                            tint = darkText,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Reproducir",
                            color = darkText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                if (phraseText.isBlank()) {
                                    statusMessage = "Escribe una frase antes de guardar."
                                } else if (currentUser == null) {
                                    statusMessage = "Debes iniciar sesión para guardar frases."
                                } else {
                                    val uid = currentUser.uid
                                    val phrasesRef = database.getReference("frases").child(uid)

                                    if (editingPhraseId == null) {
                                        val newId = phrasesRef.push().key ?: System.currentTimeMillis().toString()
                                        val phraseData = mapOf("id" to newId, "texto" to phraseText.trim())
                                        phrasesRef.child(newId).setValue(phraseData)
                                            .addOnSuccessListener {
                                                statusMessage = "Frase guardada."
                                                phraseText = ""
                                            }
                                            .addOnFailureListener {
                                                statusMessage = "No se pudo guardar la frase."
                                            }
                                    } else {
                                        val idToUpdate = editingPhraseId!!
                                        val phraseData = mapOf("id" to idToUpdate, "texto" to phraseText.trim())
                                        phrasesRef.child(idToUpdate).setValue(phraseData)
                                            .addOnSuccessListener {
                                                statusMessage = "Frase actualizada."
                                                phraseText = ""
                                                editingPhraseId = null
                                            }
                                            .addOnFailureListener {
                                                statusMessage = "No se pudo actualizar la frase."
                                            }
                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = pastelRosa)
                        ) {
                            Text(
                                text = if (editingPhraseId == null) "Guardar frase" else "Actualizar frase",
                                color = darkText,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (editingPhraseId != null) {
                            OutlinedButton(
                                onClick = {
                                    editingPhraseId = null
                                    phraseText = ""
                                    statusMessage = ""
                                },
                                modifier = Modifier.height(50.dp),
                                shape = RoundedCornerShape(24.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, pastelCeleste)
                            ) {
                                Text("Cancelar", color = pastelCeleste, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            if (statusMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = statusMessage,
                    color = pastelLila,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (savedPhrases.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBackground),
                    border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Frases guardadas:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        savedPhrases.forEach { item ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(Color(0xFF14141A), RoundedCornerShape(14.dp))
                                    .border(1.dp, cardBorder, RoundedCornerShape(14.dp))
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .background(pastelLila, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.Mic,
                                                contentDescription = null,
                                                tint = darkText,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = item.texto,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.White
                                        )
                                    }
                                    Row {
                                        TextButton(
                                            onClick = {
                                                phraseText = item.texto
                                                editingPhraseId = item.id
                                                statusMessage = ""
                                            }
                                        ) {
                                            Text("Editar", color = pastelCeleste, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }
                                        TextButton(
                                            onClick = {
                                                val uid = currentUser?.uid
                                                if (uid != null) {
                                                    database.getReference("frases").child(uid).child(item.id).removeValue()
                                                        .addOnSuccessListener {
                                                            statusMessage = "Frase eliminada."
                                                            if (editingPhraseId == item.id) {
                                                                editingPhraseId = null
                                                                phraseText = ""
                                                            }
                                                        }
                                                        .addOnFailureListener {
                                                            statusMessage = "No se pudo eliminar la frase."
                                                        }
                                                }
                                            }
                                        ) {
                                            Text("Eliminar", color = pastelRosa, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

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
fun SpeakScreenPreview() {
    ComunicadosTheme {
        SpeakScreen(onBack = {})
    }
}
