package com.example.quizz.model

data class LoginResult(
    val success: Boolean,
    val user: User? = null,
    val message: String = ""
)
