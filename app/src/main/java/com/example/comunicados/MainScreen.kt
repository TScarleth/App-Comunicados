package com.example.comunicados

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
fun MainScreen() {
    // Estados para los componentes //
    var message by remember { mutableStateOf("") }
    var useSignLanguage by remember { mutableStateOf(false) }
    var useTextToSpeech by remember { mutableStateOf(false) }
    var voiceType by remember { mutableStateOf("Masculina") }
    var expanded by remember { mutableStateOf(false) }
    var communicationType by remember { mutableStateOf("Selecciona tipo") }
    
    val communicationOptions = listOf("Señas", "Texto", "Pictogramas", "Escritura")
    val typeOptions = listOf("Formal", "Informal", "Emergencia")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título d la pantalla //
        Text(
            text = "Comunicados :)",
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

        Spacer(modifier = Modifier.height(24.dp))

        // Campo para escribir mensaje ///
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Mensaje para convertir") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de voz
        Button(onClick = { /* Acción para voz */ }) {
            Text("Convertir texto en voz")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Grilla con formas de comunicación //
        Text("Formas de comunicación:", fontWeight = FontWeight.Bold)
        Box(modifier = Modifier.height(150.dp)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(communicationOptions) { option ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
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

        // Checklist  //
        Text("Opciones utilizadas:", fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = useSignLanguage, onCheckedChange = { useSignLanguage = it })
            Text("Lengua de señas")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = useTextToSpeech, onCheckedChange = { useTextToSpeech = it })
            Text("Texto a voz")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Radio buttons para tipo de voz //
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

        // Combo box para tipo de comunicación//
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
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ComunicadosTheme {
        MainScreen()
    }
}
