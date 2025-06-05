package com.gtg.electroshopandroid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExampleDto(
    @SerialName(value = "message")
    val message: String
)