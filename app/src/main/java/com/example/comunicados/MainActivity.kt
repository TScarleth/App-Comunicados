package com.example.comunicados

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.comunicados.ui.theme.ComunicadosTheme

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        enableEdgeToEdge()
        setContent {
            ComunicadosTheme {
                val navController = rememberNavController()
                // Estados para mensajes de error //
                var loginError by remember { mutableStateOf("") }
                var registerError by remember { mutableStateOf("") }
                var registerSuccess by remember { mutableStateOf("") }
                // Nombre del usuario logueado //
                var loggedInUserName by remember { mutableStateOf("") }
                
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(navController = navController, startDestination = "login") {
                            composable("login") {
                                LoginScreen(
                                    onRegisterClick = { 
                                        registerError = ""
                                        registerSuccess = ""
                                        navController.navigate("register") 
                                    },
                                    onForgotPasswordClick = { navController.navigate("recover_password") },
                                    onLoginSubmit = { email, pass ->
                                        // Iniciar sesión con Firebase Auth //
                                        auth.signInWithEmailAndPassword(email, pass)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    val uid = auth.currentUser?.uid
                                                    if (uid != null) {
                                                        // Obtener nombre desde Realtime Database //
                                                        database.getReference("usuarios").child(uid).get()
                                                            .addOnSuccessListener { snapshot ->
                                                                loginError = ""
                                                                loggedInUserName = snapshot.child("name").getValue(String::class.java) ?: "Usuario"
                                                                navController.navigate("main") {
                                                                    popUpTo("login") { inclusive = true }
                                                                }
                                                            }
                                                            .addOnFailureListener {
                                                                loginError = "Ocurrió un problema al leer los datos."
                                                            }
                                                    } else {
                                                        loginError = "Ocurrió un problema al iniciar sesión."
                                                    }
                                                } else {
                                                    loginError = "El correo o la contraseña no son correctos."
                                                }
                                            }
                                    },
                                    errorMessage = loginError
                                )
                            }
                            composable("register") {
                                RegisterScreen(
                                    onUserRegistered = { newUser ->
                                        // Registrar usuario en Firebase Auth //
                                        auth.createUserWithEmailAndPassword(newUser.email, newUser.pass)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    val uid = auth.currentUser?.uid
                                                    if (uid != null) {
                                                        val userData = mapOf(
                                                            "name" to newUser.name,
                                                            "email" to newUser.email
                                                        )
                                                        // Guardar datos en Realtime Database //
                                                        database.getReference("usuarios").child(uid).setValue(userData)
                                                            .addOnSuccessListener {
                                                                registerError = ""
                                                                registerSuccess = "¡Se ha creado su usuario de manera exitosa!"
                                                            }
                                                            .addOnFailureListener { e ->
                                                                registerError = e.localizedMessage ?: "Error al guardar en la base de datos"
                                                                registerSuccess = ""
                                                            }
                                                    } else {
                                                        registerError = "Error al obtener usuario"
                                                        registerSuccess = ""
                                                    }
                                                } else {
                                                    val exception = task.exception
                                                    if (exception is FirebaseAuthWeakPasswordException) {
                                                        registerError = "La contraseña debe tener al menos 6 caracteres."
                                                    } else {
                                                        registerError = exception?.localizedMessage ?: "Error al registrar el usuario"
                                                    }
                                                    registerSuccess = ""
                                                }
                                            }
                                    },
                                    errorMessage = registerError,
                                    successMessage = registerSuccess,
                                    onBackToLogin = { 
                                        navController.popBackStack() 
                                    }
                                )
                            }
                            composable("recover_password") {
                                RecoverPasswordScreen(onBackToLogin = { navController.popBackStack() })
                            }
                            composable("main") {
                                MainScreen(
                                    userName = loggedInUserName,
                                    onWriteClick = { navController.navigate("text_communication") },
                                    onSpeakClick = { navController.navigate("speak") },
                                    onSearchDeviceClick = { }
                                )
                            }
                            composable("text_communication") {
                                TextCommunicationScreen(onBack = { navController.popBackStack() })
                            }
                            composable("speak") {
                                SpeakScreen(onBack = { navController.popBackStack() })
                            }
                        }
                    }
                }
            }
        }
    }
}
