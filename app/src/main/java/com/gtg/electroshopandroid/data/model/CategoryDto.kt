package com.gtg.electroshopandroid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    @SerialName("categoryId")
    val categoryId: Int,

    @SerialName("name")
    val name: String,

    @SerialName("description")
    val description: String? = null,
    )