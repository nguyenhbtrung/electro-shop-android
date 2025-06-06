package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.ProductHistoryDto
import com.gtg.electroshopandroid.data.network.ProductHistoryApiService

interface ProductHistoryRepository {
    suspend fun getHistory(): List<ProductHistoryDto>
}

class ProductHistoryRepositoryImpl(
    private val productHistoryApiService: ProductHistoryApiService
) : ProductHistoryRepository {

    override suspend fun getHistory(): List<ProductHistoryDto>
    = productHistoryApiService.getHistory()
}