package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.OrderDto
import com.gtg.electroshopandroid.data.network.OrderHistoryApiService
import com.gtg.electroshopandroid.data.model.CreateReturnRequest
import retrofit2.http.Query

interface OrderHistoryRepository {
    suspend fun getAllOrders(): List<OrderDto>
    suspend fun cancelOrder(orderId: Int): Boolean
    suspend fun createReturnRequest(request: CreateReturnRequest): Boolean
    suspend fun createOrder(
        paymentMethod: String
    ): List<OrderDto>
}

class OrderHistoryRepositoryImpl(
    private val orderHistoryApiService: OrderHistoryApiService
) : OrderHistoryRepository {

    override suspend fun getAllOrders(): List<OrderDto> = orderHistoryApiService.getAllOrders()

    override suspend fun createOrder(paymentMethod: String): List<OrderDto> =orderHistoryApiService.createOrder(paymentMethod)

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
