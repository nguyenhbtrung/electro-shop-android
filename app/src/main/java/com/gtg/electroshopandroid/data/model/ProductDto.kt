package com.gtg.electroshopandroid.data.model

import com.gtg.electroshopandroid.data.model.brand.BrandDto
import com.gtg.electroshopandroid.data.model.category.CategoryDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerialName("productId")
    val productId: Int,
    @SerialName("name")
    val name: String,
    @SerialName("info")
    val info: String? = null,
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
    val stock: Int? = null,
    @SerialName("unitsSold")
    val unitsSold: Int? = null,
    @SerialName("productImages")
    val productImages: List<ProductImageDto>,
    @SerialName("categories")
    val categories: List<CategoryDto>,
    @SerialName("brand")
    val brand: BrandDto?,
    @SerialName("isFavorite")
    val isFavorite: Boolean,
)

@Serializable
data class ProductImageDto(
    @SerialName("productImageId")
    val productImageId: Int,
    @SerialName("imageUrl")
    val imageUrl: String
)



