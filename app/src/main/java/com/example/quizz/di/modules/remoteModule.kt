package com.example.quizz.di.modules
import com.example.quizz.services.QuizzApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


internal val quizApiModule = module {
    single {
        OkHttpClient.Builder().apply {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            addInterceptor(loggingInterceptor)
        }.build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://quizapi.io/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single {
        get<Retrofit>().create(QuizzApiService::class.java)
    }
}