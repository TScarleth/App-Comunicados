package com.example.comunicados

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
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
import androidx.core.content.ContextCompat
import com.example.comunicados.ui.theme.ComunicadosTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class UserDevice(
    val id: String = "",
    val nombre: String = "",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0
)

@Composable
fun SearchDeviceScreen(onBack: () -> Unit) {
    var deviceName by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("") }
    var editingDeviceId by remember { mutableStateOf<String?>(null) }
    var savedDevices by remember { mutableStateOf<List<UserDevice>>(emptyList()) }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val database = FirebaseDatabase.getInstance()
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    DisposableEffect(currentUser?.uid) {
        val uid = currentUser?.uid
        if (uid != null) {
            val devicesRef = database.getReference("dispositivos").child(uid)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<UserDevice>()
                    for (child in snapshot.children) {
                        val id = child.child("id").getValue(String::class.java) ?: child.key ?: ""
                        val nombre = child.child("nombre").getValue(String::class.java) ?: ""
                        val lat = child.child("latitud").value?.toString()?.toDoubleOrNull() ?: 0.0
                        val lng = child.child("longitud").value?.toString()?.toDoubleOrNull() ?: 0.0
                        if (id.isNotEmpty()) {
                            list.add(UserDevice(id, nombre, lat, lng))
                        }
                    }
                    savedDevices = list
                }

                override fun onCancelled(error: DatabaseError) {
                    statusMessage = "No se pudieron cargar los dispositivos."
                }
            }
            devicesRef.addValueEventListener(listener)
            onDispose {
                devicesRef.removeEventListener(listener)
            }
        } else {
            onDispose { }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineGranted || coarseGranted) {
            fetchLocation(fusedLocationClient) { lat, lng, err ->
                if (lat != null && lng != null) {
                    latitude = lat
                    longitude = lng
                    statusMessage = "Ubicación obtenida correctamente."
                } else {
                    statusMessage = err ?: "No se pudo obtener la ubicación."
                }
            }
        } else {
            statusMessage = "Se necesita permiso de ubicación para obtener la posición."
        }
    }

    val filteredDevices = savedDevices.filter {
        it.nombre.contains(searchQuery, ignoreCase = true)
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
                    text = "Buscar dispositivo",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = pastelLila
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Registra tus dispositivos de apoyo y guarda su última ubicación conocida.",
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
                    OutlinedTextField(
                        value = deviceName,
                        onValueChange = { deviceName = it },
                        placeholder = {
                            Text("Nombre del dispositivo", color = mutedGray, fontSize = 15.sp)
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
                            val hasFine = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                            val hasCoarse = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED

                            if (hasFine || hasCoarse) {
                                fetchLocation(fusedLocationClient) { lat, lng, err ->
                                    if (lat != null && lng != null) {
                                        latitude = lat
                                        longitude = lng
                                        statusMessage = "Ubicación obtenida correctamente."
                                    } else {
                                        statusMessage = err ?: "No se pudo obtener la ubicación."
                                    }
                                }
                            } else {
                                permissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = pastelCeleste)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = darkText,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Obtener ubicación",
                            color = darkText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (latitude != null && longitude != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF14141A), RoundedCornerShape(14.dp))
                                .border(1.dp, cardBorder, RoundedCornerShape(14.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text("Última ubicación registrada:", fontWeight = FontWeight.Bold, color = pastelLila, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Latitud: $latitude", color = Color.White, fontSize = 13.sp)
                                Text("Longitud: $longitude", color = Color.White, fontSize = 13.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                if (deviceName.isBlank()) {
                                    statusMessage = "Escribe el nombre del dispositivo."
                                } else if (latitude == null || longitude == null) {
                                    statusMessage = "Obtén la ubicación antes de guardar."
                                } else if (currentUser == null) {
                                    statusMessage = "Debes iniciar sesión para guardar dispositivos."
                                } else {
                                    val uid = currentUser.uid
                                    val devicesRef = database.getReference("dispositivos").child(uid)

                                    if (editingDeviceId == null) {
                                        val newId = devicesRef.push().key ?: System.currentTimeMillis().toString()
                                        val deviceData = mapOf(
                                            "id" to newId,
                                            "nombre" to deviceName.trim(),
                                            "latitud" to latitude!!,
                                            "longitud" to longitude!!
                                        )
                                        devicesRef.child(newId).setValue(deviceData)
                                            .addOnSuccessListener {
                                                statusMessage = "Dispositivo guardado."
                                                deviceName = ""
                                                latitude = null
                                                longitude = null
                                            }
                                            .addOnFailureListener {
                                                statusMessage = "No se pudo guardar el dispositivo."
                                            }
                                    } else {
                                        val devId = editingDeviceId!!
                                        val deviceData = mapOf(
                                            "id" to devId,
                                            "nombre" to deviceName.trim(),
                                            "latitud" to latitude!!,
                                            "longitud" to longitude!!
                                        )
                                        devicesRef.child(devId).setValue(deviceData)
                                            .addOnSuccessListener {
                                                statusMessage = "Dispositivo actualizado."
                                                deviceName = ""
                                                latitude = null
                                                longitude = null
                                                editingDeviceId = null
                                            }
                                            .addOnFailureListener {
                                                statusMessage = "No se pudo actualizar el dispositivo."
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
                                text = if (editingDeviceId == null) "Guardar dispositivo" else "Actualizar dispositivo",
                                color = darkText,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (editingDeviceId != null) {
                            OutlinedButton(
                                onClick = {
                                    editingDeviceId = null
                                    deviceName = ""
                                    latitude = null
                                    longitude = null
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

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text("Buscar dispositivo...", color = mutedGray, fontSize = 15.sp)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = null,
                                tint = mutedGray
                            )
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

                    if (savedDevices.isEmpty()) {
                        Text(
                            text = "No hay dispositivos registrados.",
                            color = mutedGray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else if (filteredDevices.isEmpty()) {
                        Text(
                            text = "No se encontraron dispositivos.",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        Text(
                            text = "Dispositivos registrados:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        filteredDevices.forEach { item ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(Color(0xFF14141A), RoundedCornerShape(14.dp))
                                    .border(1.dp, cardBorder, RoundedCornerShape(14.dp))
                                    .padding(14.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = item.nombre,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "Última ubicación registrada:",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = mutedGray
                                    )
                                    Text(
                                        text = "Latitud: ${item.latitud}",
                                        fontSize = 13.sp,
                                        color = Color(0xFFD0D0E0)
                                    )
                                    Text(
                                        text = "Longitud: ${item.longitud}",
                                        fontSize = 13.sp,
                                        color = Color(0xFFD0D0E0)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        TextButton(
                                            onClick = {
                                                deviceName = item.nombre
                                                latitude = item.latitud
                                                longitude = item.longitud
                                                editingDeviceId = item.id
                                                statusMessage = ""
                                            }
                                        ) {
                                            Text("Editar", color = pastelCeleste, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }
                                        TextButton(
                                            onClick = {
                                                val uid = currentUser?.uid
                                                if (uid != null) {
                                                    database.getReference("dispositivos").child(uid).child(item.id).removeValue()
                                                        .addOnSuccessListener {
                                                            statusMessage = "Dispositivo eliminado."
                                                            if (editingDeviceId == item.id) {
                                                                editingDeviceId = null
                                                                deviceName = ""
                                                                latitude = null
                                                                longitude = null
                                                            }
                                                        }
                                                        .addOnFailureListener {
                                                            statusMessage = "No se pudo eliminar el dispositivo."
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

@SuppressLint("MissingPermission")
private fun fetchLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onResult: (Double?, Double?, String?) -> Unit
) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            onResult(location.latitude, location.longitude, null)
        } else {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { curLoc ->
                    if (curLoc != null) {
                        onResult(curLoc.latitude, curLoc.longitude, null)
                    } else {
                        onResult(null, null, "No se pudo obtener la ubicación.")
                    }
                }
                .addOnFailureListener {
                    onResult(null, null, "No se pudo obtener la ubicación.")
                }
        }
    }.addOnFailureListener {
        onResult(null, null, "No se pudo obtener la ubicación.")
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
fun SearchDeviceScreenPreview() {
    ComunicadosTheme {
        SearchDeviceScreen(onBack = {})
    }
}
