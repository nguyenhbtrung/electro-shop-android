package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.OrderDto
import com.gtg.electroshopandroid.data.network.OrderHistoryApiService

interface OrderHistoryRepository {
    suspend fun getAllOrders(): List<OrderDto>
}

class OrderHistoryRepositoryImpl(
    private val orderHistoryApiService: OrderHistoryApiService
) : OrderHistoryRepository {

    override suspend fun getAllOrders(): List<OrderDto> = orderHistoryApiService.getAllOrders()
}
