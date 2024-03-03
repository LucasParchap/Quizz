package com.example.quizz.services

import com.example.quizz.model.Question
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuizzApiService {

    @GET("questions")
    suspend fun getQuestions(
        @Query("apiKey") apiKey: String,
        @Query("limit") limit: Int? = null,
        @Query("category") category: String? = null,
        @Query("difficulty") difficulty: String? = null
    ): Response<List<Question>>
}