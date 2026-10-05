package com.example.comunicados

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.comunicados.ui.theme.ComunicadosTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// Modelo para los mensajes de usuario //
data class UserMessage(
    val id: String = "",
    val texto: String = ""
)

@Composable
fun TextCommunicationScreen(onBack: () -> Unit) {
    // Estados para los campos y mensajes //
    var message by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("") }
    var editingMessageId by remember { mutableStateOf<String?>(null) }
    var savedMessages by remember { mutableStateOf<List<UserMessage>>(emptyList()) }

    // Instancias de Firebase //
    val currentUser = FirebaseAuth.getInstance().currentUser
    val database = FirebaseDatabase.getInstance()

    // Cargar mensajes en tiempo real desde Firebase (READ) //
    DisposableEffect(currentUser?.uid) {
        val uid = currentUser?.uid
        if (uid != null) {
            val messagesRef = database.getReference("mensajes").child(uid)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<UserMessage>()
                    for (child in snapshot.children) {
                        val id = child.child("id").getValue(String::class.java) ?: child.key ?: ""
                        val texto = child.child("texto").getValue(String::class.java) ?: ""
                        if (id.isNotEmpty()) {
                            list.add(UserMessage(id, texto))
                        }
                    }
                    savedMessages = list
                }

                override fun onCancelled(error: DatabaseError) {
                    statusMessage = "No se pudieron cargar los mensajes."
                }
            }
            messagesRef.addValueEventListener(listener)
            onDispose {
                messagesRef.removeEventListener(listener)
            }
        } else {
            onDispose { }
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
            text = "Escribir",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Escribe y guarda tus mensajes para comunicarte.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Área de conversación estilo chat alternado (READ) //
        if (savedMessages.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
            ) {
                savedMessages.forEachIndexed { index, item ->
                    val isRight = index % 2 == 0
                    val alignment = if (isRight) Alignment.CenterEnd else Alignment.CenterStart
                    val containerColor = if (isRight) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
                    val contentColor = if (isRight) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
                    val cornerShape = if (isRight) RoundedCornerShape(16.dp, 16.dp, 2.dp, 16.dp) else RoundedCornerShape(16.dp, 16.dp, 16.dp, 2.dp)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        contentAlignment = alignment
                    ) {
                        Surface(
                            shape = cornerShape,
                            color = containerColor,
                            modifier = Modifier.widthIn(max = 280.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = item.texto,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = contentColor
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.align(if (isRight) Alignment.End else Alignment.Start),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    TextButton(
                                        onClick = {
                                            message = item.texto
                                            editingMessageId = item.id
                                            statusMessage = ""
                                        },
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text("Editar", style = MaterialTheme.typography.labelSmall, color = contentColor)
                                    }
                                    TextButton(
                                        onClick = {
                                            val uid = currentUser?.uid
                                            if (uid != null) {
                                                // Eliminar mensaje (DELETE) //
                                                database.getReference("mensajes").child(uid).child(item.id).removeValue()
                                                    .addOnSuccessListener {
                                                        statusMessage = "Mensaje eliminado."
                                                        if (editingMessageId == item.id) {
                                                            editingMessageId = null
                                                            message = ""
                                                        }
                                                    }
                                                    .addOnFailureListener {
                                                        statusMessage = "No se pudo eliminar el mensaje."
                                                    }
                                            }
                                        },
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text("Eliminar", style = MaterialTheme.typography.labelSmall, color = contentColor)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para escribir mensaje e ingresar //
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Escribe tu mensaje...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botón Enviar o Actualizar mensaje (CREATE / UPDATE) //
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    if (message.isBlank()) {
                        statusMessage = "Escribe un mensaje antes de guardar."
                    } else if (currentUser == null) {
                        statusMessage = "Debes iniciar sesión para guardar mensajes."
                    } else {
                        val uid = currentUser.uid
                        val messagesRef = database.getReference("mensajes").child(uid)

                        if (editingMessageId == null) {
                            // Crear mensaje (CREATE) //
                            val newId = messagesRef.push().key ?: System.currentTimeMillis().toString()
                            val msgData = mapOf("id" to newId, "texto" to message.trim())
                            messagesRef.child(newId).setValue(msgData)
                                .addOnSuccessListener {
                                    statusMessage = "Mensaje guardado."
                                    message = ""
                                }
                                .addOnFailureListener {
                                    statusMessage = "No se pudo guardar el mensaje."
                                }
                        } else {
                            // Actualizar mensaje (UPDATE) //
                            val msgId = editingMessageId!!
                            val msgData = mapOf("id" to msgId, "texto" to message.trim())
                            messagesRef.child(msgId).setValue(msgData)
                                .addOnSuccessListener {
                                    statusMessage = "Mensaje actualizado."
                                    message = ""
                                    editingMessageId = null
                                }
                                .addOnFailureListener {
                                    statusMessage = "No se pudo actualizar el mensaje."
                                }
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (editingMessageId == null) "Enviar" else "Actualizar")
            }

            if (editingMessageId != null) {
                OutlinedButton(
                    onClick = {
                        editingMessageId = null
                        message = ""
                        statusMessage = ""
                    }
                ) {
                    Text("Cancelar")
                }
            }
        }

        // Mensajes de estado //
        if (statusMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = statusMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

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
