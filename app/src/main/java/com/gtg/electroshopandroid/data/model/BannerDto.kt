package com.gtg.electroshopandroid.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BannerDto(
    val bannerId: Int,
    val imageUrl: String,
//    val link: String,
    val title: String,
)
