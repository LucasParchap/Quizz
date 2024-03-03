package com.example.quizz.db.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.quizz.model.User

@Dao
interface UserDao {
    @Upsert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun findUserByUsername(username: String): User?

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun findUserByUsernameAndPassword(username: String, password: String): User?

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun findUserByID(id: Int): User?

    @Update
    suspend fun updateScoreUser(user: User)

    @Query("SELECT * FROM users ORDER BY level DESC, bar_progress DESC")
    suspend fun getAllUsersOrderedByLevelAndProgress(): List<User>

}