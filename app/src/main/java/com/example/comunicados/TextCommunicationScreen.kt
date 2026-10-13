package com.example.comunicados

import android.view.View
import android.widget.FrameLayout
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.outlined.Send
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import com.example.comunicados.ui.theme.ComunicadosTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class UserMessage(
    val id: String = "",
    val texto: String = ""
)

@Composable
fun TextCommunicationScreen(onBack: () -> Unit) {
    var message by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("") }
    var editingMessageId by remember { mutableStateOf<String?>(null) }
    var savedMessages by remember { mutableStateOf<List<UserMessage>>(emptyList()) }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val database = FirebaseDatabase.getInstance()

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
                    text = "Escribir",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = pastelLila
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Escribe y guarda tus mensajes para comunicarte.",
                fontSize = 14.sp,
                color = mutedGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (savedMessages.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBackground),
                    border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Mensajes guardados:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        savedMessages.forEachIndexed { index, item ->
                            val isRight = index % 2 == 0
                            val alignment = if (isRight) Alignment.CenterEnd else Alignment.CenterStart
                            val bubbleColor = if (isRight) pastelRosa else pastelCeleste
                            val cornerShape = if (isRight) {
                                RoundedCornerShape(18.dp, 18.dp, 4.dp, 18.dp)
                            } else {
                                RoundedCornerShape(18.dp, 18.dp, 18.dp, 4.dp)
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                contentAlignment = alignment
                            ) {
                                Surface(
                                    shape = cornerShape,
                                    color = bubbleColor,
                                    modifier = Modifier.widthIn(max = 280.dp)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = item.texto,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = darkText
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
                                                Text("Editar", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = darkText)
                                            }
                                            TextButton(
                                                onClick = {
                                                    val uid = currentUser?.uid
                                                    if (uid != null) {
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
                                                Text("Eliminar", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = darkText)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    AndroidView(
                        factory = { ctx ->
                            val container = FrameLayout(ctx).apply {
                                id = View.generateViewId()
                            }
                            val fragmentActivity = ctx as? FragmentActivity
                            if (fragmentActivity != null) {
                                val fragment = QuickPhraseFragment().apply {
                                    onPhraseSelectedListener = { selectedPhrase ->
                                        message = selectedPhrase
                                    }
                                }
                                fragmentActivity.supportFragmentManager.beginTransaction()
                                    .replace(container.id, fragment)
                                    .commit()
                            }
                            container
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        placeholder = {
                            Text("Escribe tu mensaje...", color = mutedGray, fontSize = 15.sp)
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
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
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = pastelRosa)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.Send,
                                contentDescription = null,
                                tint = darkText,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (editingMessageId == null) "Enviar" else "Actualizar",
                                color = darkText,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (editingMessageId != null) {
                            OutlinedButton(
                                onClick = {
                                    editingMessageId = null
                                    message = ""
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
fun TextCommunicationScreenPreview() {
    ComunicadosTheme {
        TextCommunicationScreen(onBack = {})
    }
}
