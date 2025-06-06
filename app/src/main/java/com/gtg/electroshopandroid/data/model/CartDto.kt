package com.gtg.electroshopandroid.data.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class CartDto(
    @SerialName("productId")
    val productId: Int,

    @SerialName("price")
    val price: Int,

    @SerialName("quantity")
    val quantity: Int,

    @SerialName("stock")
    val stock: Int,

    @SerialName("productName")
    val productName: String,

    @SerialName("productImage")
    val productImage: String
)