package com.example.quizz

import com.example.quizz.utils.PasswordUtils.hashPassword

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class PasswordUtilsTest {
    @Test
    fun `hashPassword returns consistent results for the same input`() {
        val password = "Password123!"
        val hash1 = hashPassword(password)
        val hash2 = hashPassword(password)

        assertEquals(hash1,hash2)
    }
    @Test
    fun `hashPassword returns different results for different inputs`() {
        val password1 = "Password123!"
        val password2 = "Password124!"

        val hash1 = hashPassword(password1)
        val hash2 = hashPassword(password2)

        assertNotEquals(hash1, hash2)
    }
}