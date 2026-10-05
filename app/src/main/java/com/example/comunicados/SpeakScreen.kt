package com.example.comunicados

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.comunicados.ui.theme.ComunicadosTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

// Modelo para las frases de voz //
data class SpeakPhrase(
    val id: String = "",
    val texto: String = ""
)

@Composable
fun SpeakScreen(onBack: () -> Unit) {
    // Estados de la pantalla //
    var phraseText by remember { mutableStateOf("") }
    var voiceType by remember { mutableStateOf("Masculina") }
    var statusMessage by remember { mutableStateOf("") }
    var editingPhraseId by remember { mutableStateOf<String?>(null) }
    var savedPhrases by remember { mutableStateOf<List<SpeakPhrase>>(emptyList()) }

    // Instancias de Firebase //
    val currentUser = FirebaseAuth.getInstance().currentUser
    val database = FirebaseDatabase.getInstance()

    // Cargar frases en tiempo real desde Firebase (READ) //
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
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la pantalla //
        Text(
            text = "Hablar",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Escribe una frase para reproducirla en voz alta o guardarla.",
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

        // Campo para la frase //
        OutlinedTextField(
            value = phraseText,
            onValueChange = { phraseText = it },
            label = { Text("Frase para reproducir") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón reproducir frase //
        Button(
            onClick = {
                if (phraseText.isNotBlank() && tts != null) {
                    val targetGender = if (voiceType == "Masculina") "male" else "female"
                    val matchedVoice = tts?.voices?.find { voice ->
                        voice.name.lowercase().contains(targetGender)
                    }
                    if (matchedVoice != null) {
                        tts?.voice = matchedVoice
                        tts?.setPitch(1.0f)
                    } else {
                        tts?.setPitch(if (voiceType == "Masculina") 0.65f else 1.2f)
                    }
                    tts?.speak(phraseText, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reproducir")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón guardar o actualizar frase //
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                            // Crear nueva frase (CREATE) //
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
                            // Actualizar frase existente (UPDATE) //
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
                modifier = Modifier.weight(1f)
            ) {
                Text(if (editingPhraseId == null) "Guardar frase" else "Actualizar frase")
            }

            if (editingPhraseId != null) {
                OutlinedButton(
                    onClick = {
                        editingPhraseId = null
                        phraseText = ""
                        statusMessage = ""
                    }
                ) {
                    Text("Cancelar")
                }
            }
        }

        // Mensaje de estado //
        if (statusMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = statusMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Lista de frases guardadas (READ) //
        if (savedPhrases.isNotEmpty()) {
            Text("Frases guardadas:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            savedPhrases.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.texto,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Row {
                            TextButton(
                                onClick = {
                                    phraseText = item.texto
                                    editingPhraseId = item.id
                                    statusMessage = ""
                                }
                            ) {
                                Text("Editar")
                            }
                            TextButton(
                                onClick = {
                                    val uid = currentUser?.uid
                                    if (uid != null) {
                                        // Eliminar frase (DELETE) //
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
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón volver a HomeMenú //
        TextButton(onClick = onBack) {
            Text("Volver")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpeakScreenPreview() {
    ComunicadosTheme {
        SpeakScreen(onBack = {})
    }
}
