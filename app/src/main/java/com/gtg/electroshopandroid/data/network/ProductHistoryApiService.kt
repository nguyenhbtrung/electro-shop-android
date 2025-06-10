package com.gtg.electroshopandroid.data.network

import com.gtg.electroshopandroid.data.model.ProductHistoryDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductHistoryApiService {
    @GET("api/ProductViewHistory")
    suspend fun getHistory(): List<ProductHistoryDto>
    @POST("api/ProductViewHistory/{productId}")
    suspend fun createHistory(@Path("productId") productId: Int) : ProductHistoryDto
    @DELETE("api/ProductViewHistory/{productId}")
    suspend fun deleteHistory(@Path("productId") productId: Int) : Unit

}