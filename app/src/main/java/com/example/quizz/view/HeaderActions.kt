package com.example.quizz.view

import com.example.quizz.model.User

interface HeaderActions {
    fun logOut()
    fun leaderboard()
    fun updateUserInfoDisplay(user: User?)
    fun quizz()
}