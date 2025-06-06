package com.gtg.electroshopandroid.data.model.product

import com.gtg.electroshopandroid.data.model.ProductImageDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendDto(
    @SerialName("productId")
    val productId: Int,
    @SerialName("name")
    val name: String,
    @SerialName("originalPrice")
    val originalPrice: Double,
    @SerialName("discountedPrice")
    val discountedPrice: Double,
    @SerialName("discountType")
    val discountType: String,
    @SerialName("discountValue")
    val discountValue: Double,
    @SerialName("averageRating")
    val averageRating: Double,
    @SerialName("productImages")
    val productImages: List<ProductImageDto>,
)
