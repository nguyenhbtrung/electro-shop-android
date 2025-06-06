package com.gtg.electroshopandroid.data.network;

import com.gtg.electroshopandroid.data.model.ProductDto;
import retrofit2.http.GET;
import retrofit2.http.Path

interface ProductApiService {
    @GET("api/Product/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductDto
}