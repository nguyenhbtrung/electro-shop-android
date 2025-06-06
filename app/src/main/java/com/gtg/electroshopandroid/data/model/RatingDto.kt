package com.gtg.electroshopandroid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatingDto(
    @SerialName("productId")
    val productId: Int,

    @SerialName("userId")
    val userId: String,

    @SerialName("userName")
    val userName: String,

    @SerialName("ratingScore")
    val ratingScore: Int,

    @SerialName("ratingContent")
    val ratingContent: String,

    @SerialName("status")
    val status: String,

    @SerialName("timeStamp")
    val timeStamp: String
)