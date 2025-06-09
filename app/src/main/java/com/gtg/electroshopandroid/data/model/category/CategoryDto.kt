package com.gtg.electroshopandroid.data.model.category

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    @SerialName("categoryId")
    val categoryId: Int,

    @SerialName("name")
    val name: String,

    @SerialName("description")
    val description: String? = null,
    )
@Serializable
data class CategoryProductDto(
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