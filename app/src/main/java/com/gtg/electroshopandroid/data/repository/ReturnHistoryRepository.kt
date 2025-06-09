package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.ReturnDto
import com.gtg.electroshopandroid.data.model.ReturnDetailDto
import com.gtg.electroshopandroid.data.network.ReturnHistoryApiService

interface ReturnHistoryRepository {
    suspend fun getAllReturns(): List<ReturnDto>
    suspend fun getReturnDetail(returnId: Int): ReturnDetailDto
}

class ReturnHistoryRepositoryImpl(
    private val returnHistoryApiService: ReturnHistoryApiService
) : ReturnHistoryRepository {

    override suspend fun getAllReturns(): List<ReturnDto> = returnHistoryApiService.getAllReturns()

    override suspend fun getReturnDetail(returnId: Int): ReturnDetailDto = returnHistoryApiService.getReturnDetail(returnId)
}

