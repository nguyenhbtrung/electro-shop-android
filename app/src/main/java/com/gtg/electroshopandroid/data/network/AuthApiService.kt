package com.gtg.electroshopandroid.data.network

import com.gtg.electroshopandroid.data.model.*
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/User/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/User/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
}