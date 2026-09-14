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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.comunicados.ui.theme.ComunicadosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComunicadosTheme {
                val navController = rememberNavController()
                // Array para guardar hasta 5 usuarios //
                val userList = remember { mutableStateListOf<User>() }
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
                                        // Validar usuario //
                                        val user = userList.find { it.checkCredentials(email, pass) }
                                        if (user != null) {
                                            loginError = ""
                                            loggedInUserName = user.name
                                            navController.navigate("main")
                                        } else {
                                            loginError = "El correo o la contraseña no son correctos"
                                        }
                                    },
                                    errorMessage = loginError
                                )
                            }
                            composable("register") {
                                RegisterScreen(
                                    onUserRegistered = { newUser ->
                                        if (userList.size < 5) {
                                            userList.add(newUser)
                                            registerError = ""
                                            registerSuccess = "¡Se ha creado su usuario de manera exitosa!"
                                        } else {
                                            registerError = "Se ha alcanzado el límite de 5 usuarios"
                                            registerSuccess = ""
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
                                    onTextOptionClick = { navController.navigate("text_communication") }
                                )
                            }
                            composable("text_communication") {
                                TextCommunicationScreen(onBack = { navController.popBackStack() })
                            }
                        }
                    }
                }
            }
        }
    }
}
