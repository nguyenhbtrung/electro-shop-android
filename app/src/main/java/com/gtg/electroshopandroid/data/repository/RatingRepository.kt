package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.RatingDto
import com.gtg.electroshopandroid.data.network.RatingApiService

class RatingRepository(private val apiService: RatingApiService) {

    suspend fun getRatingsByProductId(productId: Int): List<RatingDto> {
        return apiService.getRatingsByProductId(productId)
    }
}
