package com.gtg.electroshopandroid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderItemDto(
        @SerialName("orderItemId")
        val orderItemId: Int = 0,
        @SerialName("productId")
        val productId: Int = 0,
        @SerialName("quantity")
        val quantity: Int = 0,
        @SerialName("price")
        val price: Double = 0.0,
        @SerialName("productName")
        val productName: String = "",
        @SerialName("productImage")
        val productImage: String? = null
)

@Serializable
data class OrderDto(
        @SerialName("orderId")
        val orderId: Int = 0,
        @SerialName("userId")
        val userId: String? = null,
        @SerialName("fullName")
        val fullName: String? = null,
        @SerialName("total")
        val total: Double = 0.0,
        @SerialName("status")
        val status: String? = null,
        @SerialName("paymentStatus")
        val paymentStatus: String? = null,
        @SerialName("address")
        val address: String? = null,
        @SerialName("timeStamp")
        val timeStamp: String? = null,
        @SerialName("paymentMethod")
        val paymentMethod: String? = null,
        @SerialName("paymentUrl")
        val paymentUrl: String? = null,
        @SerialName("orderItems")
        val orderItems: List<OrderItemDto> = emptyList(),
        @SerialName("payments")
        val payments: List<String> = emptyList()
)
