package com.gtg.electroshopandroid.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDetail(
    val userName: String,
    val fullName: String,
    val email: String,
    val address: String,
    val avatarImg: String,
    val phoneNumber: String
)

@Serializable
data class EditProfileRequest(
    val fullName: String,
    val address: String,
    val phoneNumber: String,
    val avatarImg: String
)