package com.gtg.electroshopandroid.data.network;

import com.gtg.electroshopandroid.data.model.product.RecommendDto
import retrofit2.http.GET;
import retrofit2.http.Path

interface RecommendApiService {
    @GET("api/Product/recommend/{productId}")
    suspend fun getRecommendById(@Path("productId") productId: Int): List<RecommendDto>
}