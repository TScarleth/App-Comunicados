package com.example.comunicados

// Modelo de datos para el usuario//
data class User(
    val name: String,
    val email: String,
    val pass: String
) {
    // Función para validar credenciales //
    fun checkCredentials(emailInput: String, passInput: String): Boolean {
        return email == emailInput && pass == passInput
    }
}

// Función de extensión para formatear elnombre //
fun String.nombreFormateado(): String {
    return this.split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
}
