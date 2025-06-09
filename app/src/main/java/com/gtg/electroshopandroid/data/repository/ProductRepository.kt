package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.ProductDto
import com.gtg.electroshopandroid.data.model.product.ProductCardDto
import com.gtg.electroshopandroid.data.network.ProductApiService

interface ProductRepository {
    suspend fun getProductById(id: Int): ProductDto
    suspend fun getDiscountedProducts(): List<ProductCardDto>
    suspend fun getBestSellerProducts(): List<ProductCardDto>
    suspend fun getProductsSearch(productName: String):List<ProductCardDto>
}

class ProductRepositoryImpl(
    private val productApiService: ProductApiService
) : ProductRepository {

    override suspend fun getProductById(id: Int): ProductDto {
        return productApiService.getProductById(id)
    }

    override suspend fun getDiscountedProducts(): List<ProductCardDto> {
        return productApiService.getDiscountedProducts()
    }

    override suspend fun getBestSellerProducts(): List<ProductCardDto> {
        return productApiService.getBestSellerProducts()
    }
    override suspend fun getProductsSearch(productName: String): List<ProductCardDto> {
        return productApiService.getProductsSearch(productName)
    }
}