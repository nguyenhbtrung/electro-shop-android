package com.gtg.electroshopandroid.data.network

import com.gtg.electroshopandroid.data.model.brand.BrandProductDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BrandApiService {
    @GET("api/Filter/brand")
    suspend fun filterProductsByBrand(
        @Query("brandId") brandId: Int,
        @Query("priceFilter") priceFilter: Int?,
        @Query("categoryId") categoryId: Int?,
        @Query("ratingFilter") ratingFilter: Int?
    ): List<BrandProductDto>

    @GET("api/Brand/{id}/Product")
    suspend fun getProductbyBrandId(
        @Path("id") brandId: Int
    ): List<BrandProductDto>
}