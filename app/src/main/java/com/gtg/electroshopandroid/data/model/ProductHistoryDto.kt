package com.gtg.electroshopandroid.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductHistoryDto(
    val historyId: Int,
    val userId: String,
    val productId: Int,
    val timeStamp: String
)
