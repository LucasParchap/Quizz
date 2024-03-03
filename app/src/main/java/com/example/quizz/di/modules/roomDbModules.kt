package com.example.quizz.di.modules

import com.example.quizz.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val roomDbModules = module{

    single { AppDatabase.getDatabase(androidContext()) }
}