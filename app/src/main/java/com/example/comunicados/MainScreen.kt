package com.example.comunicados

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.comunicados.ui.theme.ComunicadosTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    userName: String,
    onWriteClick: () -> Unit,
    onSpeakClick: () -> Unit,
    onSearchDeviceClick: () -> Unit
) {
    // Estados para los componentes //
    var useSignLanguage by remember { mutableStateOf(false) }
    var useTextToSpeech by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var communicationType by remember { mutableStateOf("Selecciona tipo") }
    // Estado para filtrar opciones //
    var commSearchQuery by remember { mutableStateOf("") }
    
    val communicationOptions = listOf("Escribir", "Hablar", "Buscar dispositivo")
    val typeOptions = listOf("Formal", "Informal", "Emergencia")

    // Filtrar opciones //
    val filteredOptions = communicationOptions.filter { 
        it.contains(commSearchQuery, ignoreCase = true) 
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
            text = "Cue :)",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Texto descriptivo //
        Text(
            text = "Una forma más simple de comunicarnos.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Mensaje de bienvenida //
        Text(
            text = "¡Bienvenido, ${userName.nombreFormateado()}! ¿Cómo podemos ayudarte hoy?",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Contexto de la comunicación //
        Text("Contexto de la comunicación:", fontWeight = FontWeight.Bold)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = communicationType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                typeOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            communicationType = selectionOption
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Formas de comunicación //
        Text("¿Cómo quieres comunicarte?", fontWeight = FontWeight.Bold)
        
        // Campo para filtrar opciones //
        OutlinedTextField(
            value = commSearchQuery,
            onValueChange = { commSearchQuery = it },
            label = { Text("Buscar opción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Grilla adaptativa de opciones //
        Box(modifier = Modifier.height(150.dp)) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 140.dp),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredOptions) { option ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { 
                                when (option) {
                                    "Escribir" -> onWriteClick()
                                    "Hablar" -> onSpeakClick()
                                    "Buscar dispositivo" -> onSearchDeviceClick()
                                }
                            },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Box(Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                            Text(option)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tabla de formas de comunicación //
        Text("Información de formas de comunicación:", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Encabezados de la tabla //
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Forma de comunicación",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Descripción",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1.5f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Filas de la tabla //
                val tableData = listOf(
                    "Escribir" to "Permite redactar mensajes de texto.",
                    "Hablar" to "Permite convertir texto en voz y comunicarse verbalmente.",
                    "Buscar dispositivo" to "Permite acceder a la función Buscar dispositivo."
                )

                tableData.forEachIndexed { index, (forma, descripcion) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = forma,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = descripcion,
                            modifier = Modifier.weight(1.5f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    if (index < tableData.size - 1) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Checklist //
        Text("¿Qué ayuda necesitas?", fontWeight = FontWeight.Bold)
        
        Text(
            text = "Selecciona las opciones que te resulten más cómodas.",
            style = MaterialTheme.typography.bodySmall
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = useSignLanguage, onCheckedChange = { useSignLanguage = it })
            Text("Lengua de señas")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = useTextToSpeech, onCheckedChange = { useTextToSpeech = it })
            Text("Texto a voz")
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ComunicadosTheme {
        MainScreen(
            userName = "Usuario",
            onWriteClick = {},
            onSpeakClick = {},
            onSearchDeviceClick = {}
        )
    }
}
