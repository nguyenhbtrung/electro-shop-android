package com.gtg.electroshopandroid.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteProductDTO(
    val productId: Int,
    val name: String,
    val price: Double,
    val imageUrl: String
)
