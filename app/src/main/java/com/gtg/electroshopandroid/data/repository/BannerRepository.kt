package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.BannerDto
import com.gtg.electroshopandroid.data.network.BannerApiService

interface BannerRepository {
    suspend fun getBanners(): List<BannerDto>
}

class BannerRepositoryImpl(
    private val bannerApiService: BannerApiService
) : BannerRepository {

    override suspend fun getBanners(): List<BannerDto> = bannerApiService.getBanners()
}

