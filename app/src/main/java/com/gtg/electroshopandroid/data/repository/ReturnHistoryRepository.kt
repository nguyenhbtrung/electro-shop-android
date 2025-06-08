package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.ReturnDto
import com.gtg.electroshopandroid.data.network.ReturnHistoryApiService

interface ReturnHistoryRepository {
    suspend fun getAllReturns(): List<ReturnDto>
}

class ReturnHistoryRepositoryImpl(
    private val returnHistoryApiService: ReturnHistoryApiService
) : ReturnHistoryRepository {

    override suspend fun getAllReturns(): List<ReturnDto> = returnHistoryApiService.getAllReturns()
}