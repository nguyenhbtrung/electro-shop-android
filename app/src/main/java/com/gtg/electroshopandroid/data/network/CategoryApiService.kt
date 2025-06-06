package com.gtg.electroshopandroid.data.network

import com.gtg.electroshopandroid.data.model.CategoryDto
import retrofit2.http.GET;
import retrofit2.http.Path

interface CategoryApiService {

    @GET("api/Category/{id}")
    suspend fun getCategory(
        @Path("id") categoryId: Int
    ): List<CategoryDto>
}