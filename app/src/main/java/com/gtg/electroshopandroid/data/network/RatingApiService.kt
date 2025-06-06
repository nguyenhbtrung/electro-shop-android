package com.gtg.electroshopandroid.data.network

import com.gtg.electroshopandroid.data.model.RatingDto;
import retrofit2.http.GET;
import retrofit2.http.Path

interface RatingApiService {

    @GET("api/Ratings/product/{productId}")
    suspend fun getRatingsByProductId(
        @Path("productId") productId: Int
    ): List<RatingDto>
}