package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.product.RecommendDto
import com.gtg.electroshopandroid.data.network.ProductApiService
import com.gtg.electroshopandroid.data.network.RecommendApiService

interface RecommendRepository {
    suspend fun getRecommendById(productId: Int): RecommendDto
}

class RecommendRepositoryImpl(
    private val recommendApiService: RecommendApiService
) : RecommendRepository {

    override suspend fun getRecommendById(productId: Int): RecommendDto {
        return recommendApiService.getRecommendById(productId)
    }
}