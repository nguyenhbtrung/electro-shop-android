package com.gtg.electroshopandroid.data.network

import com.gtg.electroshopandroid.data.model.ProductHistoryDto
import retrofit2.http.GET

interface ProductHistoryApiService {
    @GET("api/ProductViewHistory")
    suspend fun getHistory(): List<ProductHistoryDto>
}