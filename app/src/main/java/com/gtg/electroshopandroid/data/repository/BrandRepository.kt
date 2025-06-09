package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.brand.BrandProductDto
import com.gtg.electroshopandroid.data.network.BrandApiService

class BrandRepository(private val apiService: BrandApiService) {
    suspend fun getProductbyBrandId(brandId: Int): List<BrandProductDto> {
        return apiService.getProductbyBrandId(brandId)
    }
    suspend fun filterProductsByBrand(brandId: Int, priceFilter: Int? ,categoryId: Int?, ratingFilter: Int?): List<BrandProductDto> {
        return apiService.filterProductsByBrand(brandId, priceFilter,categoryId , ratingFilter)
    }
}
