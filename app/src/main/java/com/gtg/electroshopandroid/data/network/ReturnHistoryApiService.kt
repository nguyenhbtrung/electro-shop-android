package com.gtg.electroshopandroid.data.network

import com.gtg.electroshopandroid.data.model.ReturnDto
import retrofit2.http.GET

interface ReturnHistoryApiService {
    @GET("api/Return/history")
    suspend fun getAllReturns(): List<ReturnDto>
}