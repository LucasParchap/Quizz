package com.example.quizz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizz.R
import com.example.quizz.model.LoginResult
import com.example.quizz.repositories.UserRepository
import com.example.quizz.utils.PasswordUtils
import com.example.quizz.utils.ResourceProvider
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult


    fun validateLogin(username: String, password: String) {
        viewModelScope.launch {
            try {
                val hashedPassword = PasswordUtils.hashPassword(password)
                val user = userRepository.validateUser(username, hashedPassword)
                if (user != null) {
                    _loginResult.value = LoginResult(success = true, user = user, message = resourceProvider.getString(R.string.login_success))

                } else {
                    _loginResult.value = LoginResult(success = false, message = resourceProvider.getString(R.string.incorrect_credentials))
                }
            } catch (e: Exception) {
                _loginResult.value = LoginResult(success = false, message = resourceProvider.getString(R.string.login_error))
            }
        }
    }




}