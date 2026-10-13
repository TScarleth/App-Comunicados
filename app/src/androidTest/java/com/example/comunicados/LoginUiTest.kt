package com.example.comunicados

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginScreen_isDisplayed_showsWelcomeTextAndButtons() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("¡Bienvenid@ a Cue!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tu nuevo apoyo en la comunicación :)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Iniciar sesión").assertIsDisplayed()
        composeTestRule.onNodeWithText("Crear cuenta").assertIsDisplayed()
    }

    @Test
    fun navigateToRegisterScreen_andBackToLogin() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Crear cuenta").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("¡Únete a nosotros!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Volver al Login").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("¡Bienvenid@ a Cue!").assertIsDisplayed()
    }
}
