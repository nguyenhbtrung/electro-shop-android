package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.ProductDto
import com.gtg.electroshopandroid.data.network.ProductApiService

interface ProductRepository {
    suspend fun getProductById(id: Int): ProductDto
}

class ProductRepositoryImpl(
    private val productApiService: ProductApiService
) : ProductRepository {

    override suspend fun getProductById(id: Int): ProductDto {
        return productApiService.getProductById(id)
    }
}