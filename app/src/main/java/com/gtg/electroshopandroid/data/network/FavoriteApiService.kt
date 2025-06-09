package com.gtg.electroshopandroid.data.network

import com.gtg.electroshopandroid.data.model.FavoriteProductDTO
import retrofit2.http.GET

interface FavoriteApiService {
    @GET("api/favorites")
    suspend fun getFavorites(): List<FavoriteProductDTO>
}