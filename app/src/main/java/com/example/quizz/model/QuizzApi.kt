package com.example.quizz.model

// The variable names cannot be changed to ensure direct compatibility with the external API's JSON structure.
data class Question(
    val id: Int,
    val question: String,
    val description: String?,
    val answers: Answers,
    val multiple_correct_answers: String,
    val correct_answers: CorrectAnswers,
    val explanation: String?,
    val tip: String?,
    val tags: List<Tag>?,
    val category: String,
    val difficulty: String
)

data class Answers(
    val answer_a: String?,
    val answer_b: String?,
    val answer_c: String?,
    val answer_d: String?,
    val answer_e: String?,
    val answer_f: String?
)

data class CorrectAnswers(
    val answer_a_correct: String,
    val answer_b_correct: String,
    val answer_c_correct: String,
    val answer_d_correct: String,
    val answer_e_correct: String, 
    val answer_f_correct: String
)

data class Tag(
    val name: String
)

