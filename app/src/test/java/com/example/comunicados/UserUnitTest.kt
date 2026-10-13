package com.example.comunicados

import org.junit.Assert.assertEquals
import org.junit.Test

class UserUnitTest {

    @Test
    fun nombreFormateado_lowercaseName_capitalizesFirstLetterOfEachWord() {
        val input = "thiare scarlett"
        val expected = "Thiare Scarlett"
        val result = input.nombreFormateado()
        assertEquals(expected, result)
    }

    @Test
    fun nombreFormateado_singleWord_capitalizesFirstLetter() {
        val input = "scarlett"
        val expected = "Scarlett"
        val result = input.nombreFormateado()
        assertEquals(expected, result)
    }

    @Test
    fun nombreFormateado_multipleWords_capitalizesAllWords() {
        val input = "juan carlos perez"
        val expected = "Juan Carlos Perez"
        val result = input.nombreFormateado()
        assertEquals(expected, result)
    }

    @Test
    fun nombreFormateado_emptyString_returnsEmptyString() {
        val input = ""
        val expected = ""
        val result = input.nombreFormateado()
        assertEquals(expected, result)
    }

    @Test
    fun checkCredentials_correctEmailAndPassword_returnsTrue() {
        val user = User("Thiare", "thiare@example.com", "123456")
        val result = user.checkCredentials("thiare@example.com", "123456")
        assertEquals(true, result)
    }

    @Test
    fun checkCredentials_incorrectPassword_returnsFalse() {
        val user = User("Thiare", "thiare@example.com", "123456")
        val result = user.checkCredentials("thiare@example.com", "wrongpass")
        assertEquals(false, result)
    }

    @Test
    fun checkCredentials_incorrectEmail_returnsFalse() {
        val user = User("Thiare", "thiare@example.com", "123456")
        val result = user.checkCredentials("wrong@example.com", "123456")
        assertEquals(false, result)
    }

    @Test
    fun userModel_propertiesMatchConstructorParameters() {
        val user = User("Scarlett", "scarlett@example.com", "pass123")
        assertEquals("Scarlett", user.name)
        assertEquals("scarlett@example.com", user.email)
        assertEquals("pass123", user.pass)
    }
}
