package com.gtg.electroshopandroid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReturnProductDto(
    @SerialName("productId")
    val productId: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("image")
    val image: String? = null,
    @SerialName("returnQuantity")
    val returnQuantity: Int = 0
)

@Serializable
data class ReturnDto(
    @SerialName("returnId")
    val returnId: Int = 0,
    @SerialName("status")
    val status: String = "",
    @SerialName("returnMethod")
    val returnMethod: String = "",
    @SerialName("timeStamp")
    val timeStamp: String = "",
    @SerialName("returnProducts")
    val returnProducts: List<ReturnProductDto> = emptyList()
)
