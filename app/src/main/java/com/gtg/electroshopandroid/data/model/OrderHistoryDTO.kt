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

@Serializable
data class CreateReturnRequest(
        @SerialName("orderId")
        val orderId: Int,

        @SerialName("reason")
        val reason: String,

        @SerialName("detail")
        val detail: String? = null,

        @SerialName("returnMethod")
        val returnMethod: Int,

        @SerialName("returnItems")
        val returnItems: List<ReturnItemRequest>,

        @SerialName("evidenceImages")
        val evidenceImages: List<String> = emptyList()
)

@Serializable
data class ReturnItemRequest(
        @SerialName("orderItemId")
        val orderItemId: Int,

        @SerialName("returnQuantity")
        val returnQuantity: Int
)

data class CreateReturnFormState(
        val orderId: Int = 0,
        val reason: String = "1", // Fixed value
        val detail: String = "",
        val returnMethod: Int = 0, // 0=Refund, 1=Exchange, 2=Repair
        val orderItems: List<ReturnableItem> = emptyList(),
        val evidenceImages: List<String> = emptyList(),
        val isLoading: Boolean = false,
        val errors: Map<String, String> = emptyMap(),
        val submitSuccess: Boolean = false
)

data class ReturnableItem(
        val orderItemId: Int,
        val productName: String,
        val productImage: String?,
        val maxQuantity: Int,
        val returnQuantity: Int = 0,
        val isSelected: Boolean = false
)

enum class ReturnMethod(val value: Int, val displayName: String) {
        REFUND(0, "Hoàn tiền"),
        EXCHANGE(1, "Đổi sản phẩm"),
        REPAIR(2, "Sửa chữa");

        companion object {
                fun fromValue(value: Int): ReturnMethod {
                        return entries.find { it.value == value } ?: REFUND
                }
        }
}