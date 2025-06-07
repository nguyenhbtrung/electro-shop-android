package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.EditProfileRequest
import com.gtg.electroshopandroid.data.model.ProfileDetail
import com.gtg.electroshopandroid.data.network.ProfileDetailApiService


interface ProfileRepository{
    suspend fun getProfile(): ProfileDetail
    suspend fun updateUser(request: EditProfileRequest)

}

class ProfileRepositoryImpl(
    private val apiService: ProfileDetailApiService
) : ProfileRepository {

    override suspend fun getProfile(): ProfileDetail {
        return apiService.getProfile()
    }

    override suspend fun updateUser(request: EditProfileRequest) {
        val response = apiService.updateUser(request)
        if (!response.isSuccessful) {
            throw Exception("Update failed with code ${response.code()}")
        }
    }
}