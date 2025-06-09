package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.AuthResponse
import com.gtg.electroshopandroid.data.model.FavoriteProductDTO
import com.gtg.electroshopandroid.data.model.LoginRequest
import com.gtg.electroshopandroid.data.model.RegisterRequest
import com.gtg.electroshopandroid.data.network.FavoriteApiService

interface FavoriteRepository {
    suspend fun getFavorites(): List<FavoriteProductDTO>
}

class FavoriteRepositoryImpl(
    private val favoriteApiService: FavoriteApiService
) : FavoriteRepository {
    override suspend fun getFavorites(): List<FavoriteProductDTO> {
        return favoriteApiService.getFavorites()
    }
}