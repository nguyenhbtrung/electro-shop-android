package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.CategoryDto
import com.gtg.electroshopandroid.data.network.CategoryApiService

class CategoryRepository(private val apiService: CategoryApiService) {

    suspend fun getCategory(categoryId: Int): List<CategoryDto> {
        return apiService.getCategory(categoryId)
    }
}
