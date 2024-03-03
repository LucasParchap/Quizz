package com.example.quizz.di.modules
import com.example.quizz.repositories.QuizzRepository
import com.example.quizz.repositories.UserRepository
import com.example.quizz.utils.ResourceProvider
import com.example.quizz.viewmodel.LoginViewModel
import com.example.quizz.viewmodel.QuizzViewModel
import com.example.quizz.viewmodel.RegisterViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModels = module {
    viewModel { RegisterViewModel(get(),get()) }
    single { UserRepository(get()) }
    viewModel { LoginViewModel(get(),get()) }
    viewModel { QuizzViewModel(get()) }
    single { QuizzRepository(get(),get()) }
    single { ResourceProvider(androidContext()) }
}
