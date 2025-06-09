package com.gtg.electroshopandroid.data.network

import com.gtg.electroshopandroid.data.model.category.CategoryProductDto
import retrofit2.http.GET;
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryApiService {

    @GET("api/Category/{id}/Product")
    suspend fun getProductbyCategoryId(
        @Path("id") categoryId: Int
    ): List<CategoryProductDto>
    @GET("api/Filter/category")
    suspend fun filterProductsByCategory(
        @Query("categoryId") categoryId: Int,
        @Query("priceFilter") priceFilter: Int?,
        @Query("brandId") brandId: Int?,
        @Query("ratingFilter") ratingFilter: Int?
    ): List<CategoryProductDto>
}