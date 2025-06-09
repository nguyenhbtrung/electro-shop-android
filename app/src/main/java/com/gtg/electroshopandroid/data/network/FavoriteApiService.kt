package com.gtg.electroshopandroid.data.network

import com.gtg.electroshopandroid.data.model.FavoriteProductDTO
import com.gtg.electroshopandroid.data.model.ToggleFavoriteResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FavoriteApiService {
    @GET("api/Favorite")
    suspend fun getFavorites(): List<FavoriteProductDTO>

    @POST("api/Favorite/toggle/{productId}")
    suspend fun toggleFavorite(@Path("productId") productId: Int): ToggleFavoriteResponse
}