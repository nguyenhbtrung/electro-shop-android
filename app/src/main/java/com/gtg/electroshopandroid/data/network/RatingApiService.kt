package com.gtg.electroshopandroid.data.network

import com.gtg.electroshopandroid.data.model.RatingDto;
import retrofit2.http.GET;
import retrofit2.http.Path
import retrofit2.http.Query

interface RatingApiService {
    suspend fun getRatingById(id: Int): RatingDto

    @GET("api/Ratings/product/{productId}")
    suspend fun getRatingsByProductId(
        @Path("productId") productId: Int,
        @Query("page") page: Int
    ): List<RatingDto>
}