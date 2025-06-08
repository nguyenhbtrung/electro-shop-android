package com.gtg.electroshopandroid.data.network

import com.gtg.electroshopandroid.data.model.category.CategoryDto
import com.gtg.electroshopandroid.data.model.ProductDto
import com.gtg.electroshopandroid.data.model.category.CategoryProductDto
import retrofit2.http.GET;
import retrofit2.http.Path

interface CategoryApiService {

    @GET("api/Category/{id}")
    suspend fun getCategory(
        @Path("id") categoryId: Int
    ): List<CategoryDto>
    @GET("api/Category/{id}/Product")
    suspend fun getProductbyCategoryId(
        @Path("id") categoryId: Int
    ): List<CategoryProductDto>
}