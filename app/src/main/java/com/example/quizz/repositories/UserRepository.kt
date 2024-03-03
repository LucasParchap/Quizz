package com.example.quizz.repositories

import android.content.Context
import com.example.quizz.db.AppDatabase
import com.example.quizz.db.daos.UserDao
import com.example.quizz.model.User

class UserRepository(private val context: Context) {

    private val userDao: UserDao by lazy {
        AppDatabase.getDatabase(context).userDao()
    }
    suspend fun registerUser(user: User) {
        userDao.insertUser(user)
    }
    suspend fun validateUser(username: String, password: String): User? {
        return userDao.findUserByUsernameAndPassword(username, password)
    }
    suspend fun emailExists(email: String): Boolean {
        return userDao.findUserByEmail(email) != null
    }
    suspend fun usernameExists(username: String): Boolean {
        return userDao.findUserByUsername(username) != null
    }
    suspend fun findUserByID(userId: Int): User? {
        return userDao.findUserByID(userId)
    }
    suspend fun updateScoreUser(user: User) {
        userDao.updateScoreUser(user)
    }
    suspend fun getAllUsersOrderedByLevelAndProgress(): List<User> {
        return userDao.getAllUsersOrderedByLevelAndProgress()
    }

}

