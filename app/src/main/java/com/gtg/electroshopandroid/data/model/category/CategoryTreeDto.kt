package com.gtg.electroshopandroid.data.model.category

import kotlinx.serialization.Serializable

@Serializable
data class CategoryTreeDto(
    val categoryId: Int,
    val name: String,
    val childs: List<CategoryTreeDto>
)
