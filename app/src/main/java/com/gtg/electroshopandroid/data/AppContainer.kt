package com.gtg.electroshopandroid.data

import android.content.Context
import com.gtg.electroshopandroid.data.interceptor.AuthInterceptor
import com.gtg.electroshopandroid.data.network.AuthApiService
import com.gtg.electroshopandroid.data.network.ExampleApiService
import com.gtg.electroshopandroid.data.network.ProductHistoryApiService
import com.gtg.electroshopandroid.data.repository.AuthRepository
import com.gtg.electroshopandroid.data.repository.AuthRepositoryImpl
import com.gtg.electroshopandroid.data.repository.ExampleRepository
import com.gtg.electroshopandroid.data.repository.ExampleRepositoryImpl
import com.gtg.electroshopandroid.data.repository.ProductHistoryRepository
import com.gtg.electroshopandroid.data.repository.ProductHistoryRepositoryImpl
import com.gtg.electroshopandroid.preferences.TokenPreferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

interface AppContainer {
    val exampleRepository: ExampleRepository
    val productHistoryRepository: ProductHistoryRepository
    val authRepository: AuthRepository
    val tokenPreferences: TokenPreferences
}

class DefaultAppContainer(
    private val context: Context
) : AppContainer {
    private val baseUrl = "http://10.0.2.2:5030/"

    override val tokenPreferences by lazy {
        TokenPreferences(context)
    }

    private val authInterceptor by lazy {
        AuthInterceptor(tokenPreferences)
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(Json{ignoreUnknownKeys = true}.asConverterFactory("application/json".toMediaType()))
        .build()

    private val exampleApiService: ExampleApiService by lazy {
        retrofit.create(ExampleApiService::class.java)
    }

    override val exampleRepository: ExampleRepository by lazy {
        ExampleRepositoryImpl(exampleApiService)
    }

    private val productHistoryApiService: ProductHistoryApiService by lazy {
        retrofit.create(ProductHistoryApiService::class.java)
    }

    override val productHistoryRepository: ProductHistoryRepository by lazy {
        ProductHistoryRepositoryImpl(productHistoryApiService)
    }

    private val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(authApiService)
    }
}