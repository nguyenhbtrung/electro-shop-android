package com.gtg.electroshopandroid.data.model.product

import kotlinx.serialization.Serializable

@Serializable
data class ProductCardDto(
    val productId: Int,
    val name: String,
    val images: List<String>,
    val originalPrice: Double,
    val discountedPrice: Double,
    val discountType: String,
    val discountValue: Double,
    val averageRating: Double
)
