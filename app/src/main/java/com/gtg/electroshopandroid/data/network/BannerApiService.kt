package com.gtg.electroshopandroid.data.network

import com.gtg.electroshopandroid.data.model.BannerDto
import retrofit2.http.GET

interface BannerApiService {
    @GET("api/Banner")
    suspend fun getBanners(): List<BannerDto>
}