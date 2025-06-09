package com.gtg.electroshopandroid.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteProductDTO(
    val productId: Int,
    val name: String,
    val originalPrice: Double,
    val discountedPrice: Double,
    val imageUrl: String
)

@Serializable
data class ToggleFavoriteResponse(
    val status: String // "Added" hoặc "Removed"
)
