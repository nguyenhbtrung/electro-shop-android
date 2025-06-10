package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.ProductHistoryDto
import com.gtg.electroshopandroid.data.network.ProductHistoryApiService

interface ProductHistoryRepository {
    suspend fun getHistory(): List<ProductHistoryDto>
    suspend fun createHistory(productId: Int): ProductHistoryDto
    suspend fun deleteHistory(productId: Int): Unit
}

class ProductHistoryRepositoryImpl(
    private val productHistoryApiService: ProductHistoryApiService
) : ProductHistoryRepository {

    override suspend fun getHistory(): List<ProductHistoryDto>
    = productHistoryApiService.getHistory()

    override suspend fun createHistory(productId: Int): ProductHistoryDto
    = productHistoryApiService.createHistory(productId)

    override suspend fun deleteHistory(productId: Int): Unit
            = productHistoryApiService.deleteHistory(productId)
}