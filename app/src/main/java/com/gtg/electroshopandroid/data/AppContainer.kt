package com.gtg.electroshopandroid.data

import com.gtg.electroshopandroid.data.network.ExampleApiService
import com.gtg.electroshopandroid.data.repository.ExampleRepository
import com.gtg.electroshopandroid.data.repository.ExampleRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val exampleRepository: ExampleRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = "http://10.0.2.2:5030/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val exampleApiService: ExampleApiService by lazy {
        retrofit.create(ExampleApiService::class.java)
    }

    override val exampleRepository: ExampleRepository by lazy {
        ExampleRepositoryImpl(exampleApiService)
    }
}