package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.AuthResponse
import com.gtg.electroshopandroid.data.model.LoginRequest
import com.gtg.electroshopandroid.data.model.RegisterRequest
import com.gtg.electroshopandroid.data.network.AuthApiService

interface AuthRepository {
    suspend fun login(request: LoginRequest): AuthResponse
    suspend fun register(request: RegisterRequest): AuthResponse
}

class AuthRepositoryImpl(
    private val authApiService: AuthApiService
) : AuthRepository {

    override suspend fun login(request: LoginRequest): AuthResponse {
        return authApiService.login(request)
    }

    override suspend fun register(request: RegisterRequest): AuthResponse {
        return authApiService.register(request)
    }
}