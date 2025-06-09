package com.gtg.electroshopandroid.data.network

import com.gtg.electroshopandroid.data.model.ReturnDto
import com.gtg.electroshopandroid.data.model.ReturnDetailDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ReturnHistoryApiService {
    @GET("api/Return/history")
    suspend fun getAllReturns(): List<ReturnDto>

    @GET("api/Return/{returnId}")
    suspend fun getReturnDetail(@Path("returnId") returnId: Int): ReturnDetailDto
}