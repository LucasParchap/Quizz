package com.example.quizz.repositories

import com.example.quizz.model.Question
import com.example.quizz.model.User
import com.example.quizz.services.QuizzApiService


class QuizzRepository(private val quizzApiService: QuizzApiService,private val userRepository: UserRepository) {

    suspend fun getQuestions(apiKey: String, limit: Int = 10): List<Question>? {
        return try {
            val response = quizzApiService.getQuestions(apiKey, limit)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
    suspend fun getUserByID(userId: Int): User? {
        return try {
            userRepository.findUserByID(userId)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateUser(user: User) {
        try {
            userRepository.updateScoreUser(user)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}