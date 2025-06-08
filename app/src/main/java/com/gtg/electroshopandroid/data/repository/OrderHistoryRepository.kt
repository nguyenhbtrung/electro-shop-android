package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.OrderDto
import com.gtg.electroshopandroid.data.network.OrderHistoryApiService
import com.gtg.electroshopandroid.data.model.CreateReturnRequest

interface OrderHistoryRepository {
    suspend fun getAllOrders(): List<OrderDto>
    suspend fun cancelOrder(orderId: Int): Boolean
    suspend fun createReturnRequest(request: CreateReturnRequest): Boolean
}

class OrderHistoryRepositoryImpl(
    private val orderHistoryApiService: OrderHistoryApiService
) : OrderHistoryRepository {

    override suspend fun getAllOrders(): List<OrderDto> = orderHistoryApiService.getAllOrders()

    override suspend fun cancelOrder(orderId: Int): Boolean {
        return try {
            val response = orderHistoryApiService.cancelOrder(orderId)
            response.isSuccessful
        } catch (_: Exception) {
            false
        }
    }

    override suspend fun createReturnRequest(request: CreateReturnRequest): Boolean {
        return try {
            val response = orderHistoryApiService.createReturnRequest(request)
            response.isSuccessful
        } catch (_: Exception) {
            false
        }
    }
}
