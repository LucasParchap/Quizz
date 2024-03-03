package com.example.quizz.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizz.R
import com.example.quizz.model.User
import com.example.quizz.repositories.UserRepository
import com.example.quizz.utils.PasswordUtils
import com.example.quizz.utils.ResourceProvider
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    val validationMessage = MutableLiveData<String?>()
    val navigateToLogin = MutableLiveData<String?>()

    fun validateRegistration(username: String, email: String, password: String, confirmPassword: String) {

        viewModelScope.launch {
            if (userRepository.usernameExists(username)) {
                validationMessage.value = resourceProvider.getString(R.string.username_taken)
                return@launch
            }
            if (userRepository.emailExists(email)) {
                validationMessage.value = resourceProvider.getString(R.string.email_exists)
                return@launch
            }
            if (username.length <= 3) {
                validationMessage.value = resourceProvider.getString(R.string.username_short)
                return@launch
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                validationMessage.value = resourceProvider.getString(R.string.email_invalid)
                return@launch
            }

            val passwordPattern =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$"
            if (!password.matches(passwordPattern.toRegex())) {
                validationMessage.value = resourceProvider.getString(R.string.password_requirements)
                return@launch
            }

            if (password != confirmPassword) {
                validationMessage.value = resourceProvider.getString(R.string.password_mismatch)
                return@launch
            }

            registerUser(username, email, password)
        }
    }
    private fun registerUser(username: String, email: String, password: String) {

        val hashedPassword = PasswordUtils.hashPassword(password)
        val newUser = User(username = username, email = email, password = hashedPassword)

        viewModelScope.launch {
            userRepository.registerUser(newUser)
            validationMessage.postValue(resourceProvider.getString(R.string.user_registered))
            navigateToLogin.postValue(username)
        }
    }


}