package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.FavoriteProductDTO
import com.gtg.electroshopandroid.data.network.FavoriteApiService

interface FavoriteRepository {
    suspend fun getFavorites(): List<FavoriteProductDTO>
    suspend fun toggleFavorite(productId: Int): String

}

class FavoriteRepositoryImpl(
    private val favoriteApiService: FavoriteApiService
) : FavoriteRepository {
    override suspend fun getFavorites(): List<FavoriteProductDTO> {
        return favoriteApiService.getFavorites()
    }
    override suspend fun toggleFavorite(productId: Int): String {
        return favoriteApiService.toggleFavorite(productId).status
    }
}