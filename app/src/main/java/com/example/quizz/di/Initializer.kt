package com.example.quizz.di

import android.content.Context
import com.example.quizz.di.modules.ViewModels
import com.example.quizz.di.modules.quizApiModule
import com.example.quizz.di.modules.roomDbModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.loadKoinModules
import org.koin.core.error.ApplicationAlreadyStartedException


fun injectModuleDependencies(context: Context) {
    try {
        startKoin {
            androidContext(context)
            modules(modules)
        }
    } catch (alreadyStart: ApplicationAlreadyStartedException ) {
        loadKoinModules(modules)
    }
}
private val modules = mutableListOf(ViewModels, quizApiModule, roomDbModules)
