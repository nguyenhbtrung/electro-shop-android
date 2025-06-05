package com.gtg.electroshopandroid.data.network

import com.gtg.electroshopandroid.data.model.ExampleDto
import retrofit2.http.GET

interface ExampleApiService {
    @GET("api/v2/Example")
    suspend fun getExample(): ExampleDto
}