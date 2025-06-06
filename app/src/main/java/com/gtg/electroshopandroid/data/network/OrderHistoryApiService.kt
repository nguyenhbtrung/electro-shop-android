package com.gtg.electroshopandroid.data.network

import com.gtg.electroshopandroid.data.model.OrderDto
import retrofit2.http.GET

interface OrderHistoryApiService {
    @GET("api/Order/user/vieworder")
    suspend fun getAllOrders(): List<OrderDto>
}
