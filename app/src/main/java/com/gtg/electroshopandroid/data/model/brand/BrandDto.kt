package com.gtg.electroshopandroid.data.model.brand

import kotlinx.serialization.SerialName
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
    val isFavorite: Boolean
)
@Serializable
data class BrandDto(
    @SerialName("brandId")
    val brandId: Int,
    @SerialName("brandName")
    val brandName: String,
    @SerialName("country")
    val country: String,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("info")
    val info: String
)
