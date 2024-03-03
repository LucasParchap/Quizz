package com.example.quizz.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizz.databinding.RegistrerBinding
import com.example.quizz.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class Registration : AppCompatActivity() {

    private lateinit var binding: RegistrerBinding

    private val registerViewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = RegistrerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeValidationMessages()
        observeNavigationToLogin()

        setupRegistrationAction()
        setupGoToLoginAction()

    }

    private fun observeValidationMessages() {
        registerViewModel.validationMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun observeNavigationToLogin() {
        registerViewModel.navigateToLogin.observe(this) { username ->
            username?.let {
                val intent = Intent(this, Login::class.java).apply {
                    putExtra("USERNAME", it)
                }
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setupRegistrationAction() {
        binding.registerButton.setOnClickListener {
            val username = binding.username.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()

            registerViewModel.validateRegistration(username, email, password, confirmPassword)
        }
    }
    private fun setupGoToLoginAction() {
        binding.loginLink.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }


}