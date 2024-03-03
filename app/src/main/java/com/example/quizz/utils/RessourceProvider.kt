package com.example.quizz.utils

import android.content.Context

class ResourceProvider(private val context: Context){
    fun getString(resId: Int): String = context.getString(resId)
}