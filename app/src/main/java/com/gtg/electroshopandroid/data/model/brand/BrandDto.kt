package com.gtg.electroshopandroid.data.model.brand

import kotlinx.serialization.Serializable

@Serializable
data class BrandProductDto(
    val productId: Int,
    val name: String,
    val originalPrice: Double,
    val discountedPrice: Double,
    val discountType: String,
    val discountValue: Double,
    val averageRating: Double,
    val images: List<String>,
)