package com.gtg.electroshopandroid.data.network

import com.gtg.electroshopandroid.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ProfileDetailApiService {
    @GET("api/User/user/me")
    suspend fun getProfile(): ProfileDetail

    @PUT("api/User/user")
    suspend fun updateUser(
        @Body request: EditProfileRequest
    ): Response<String>
}
