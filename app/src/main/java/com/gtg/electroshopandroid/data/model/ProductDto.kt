package com.gtg.electroshopandroid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerialName("productId")
    val productId: Int,
    @SerialName("name")
    val name: String,
    @SerialName("info")
    val info: String,
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
    @SerialName("stock")
    val stock: Int,
    @SerialName("unitsSold")
    val unitsSold: Int,
    @SerialName("productImages")
    val productImages: List<ProductImageDto>,
    @SerialName("categories")
    val categories: List<CategoryDto>,
    @SerialName("brand")
    val brand: BrandDto,
)

@Serializable
data class ProductImageDto(
    @SerialName("productImageId")
    val productImageId: Int,
    @SerialName("imageUrl")
    val imageUrl: String
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
