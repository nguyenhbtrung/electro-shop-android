package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.category.CategoryProductDto
import com.gtg.electroshopandroid.data.network.CategoryApiService

class CategoryRepository(private val apiService: CategoryApiService) {

    suspend fun getProductbyCategoryId(categoryId: Int): List<CategoryProductDto> {
        return apiService.getProductbyCategoryId(categoryId)
    }
    suspend fun filterProductsByCategory(categoryId: Int, priceFilter: Int?, brandId: Int?, ratingFilter: Int?): List<CategoryProductDto> {
        return apiService.filterProductsByCategory(categoryId, priceFilter, brandId, ratingFilter)
    }
}
