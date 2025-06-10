package com.gtg.electroshopandroid.data.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class CartDto(
    @SerialName("productId")
    val productId: Int,

    @SerialName("price")
    val price: Double,

    @SerialName("quantity")
    val quantity: Int,

    @SerialName("stock")
    val stock: Int = 0,

    @SerialName("productName")
    val productName: String? = null,    // ✅ Cho phép null

    @SerialName("productImage")
    val productImage: String? = null    // ✅ Cho phép null
)