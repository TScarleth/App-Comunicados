package com.example.comunicados

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.comunicados.ui.theme.ComunicadosTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TutorialUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun tutorialScreen_isDisplayed_showsTitleAndSections() {
        var backClicked = false
        composeTestRule.setContent {
            ComunicadosTheme {
                TutorialScreen(onBack = { backClicked = true })
            }
        }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("¿Cómo usar Cue :) ?").assertIsDisplayed()
        composeTestRule.onNodeWithText("1. Escribir").assertIsDisplayed()
        composeTestRule.onNodeWithText("2. Hablar").assertIsDisplayed()
        composeTestRule.onNodeWithText("3. Buscar dispositivo").assertIsDisplayed()
        composeTestRule.onNodeWithText("4. Volver al inicio").assertIsDisplayed()

        composeTestRule.onNodeWithText("Volver al Home").performClick()
        assertTrue(backClicked)
    }
}
