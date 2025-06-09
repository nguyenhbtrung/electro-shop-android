package com.gtg.electroshopandroid.data.network

import com.gtg.electroshopandroid.data.model.OrderDto
import com.gtg.electroshopandroid.data.model.CreateReturnRequest
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.Query

interface OrderHistoryApiService {
    @POST("api/Order/user/createorder")
    suspend fun createOrder(
        @Query("payment") paymentMethod: String
    ): List<OrderDto>
    @GET("api/Order/user/vieworder")
    suspend fun getAllOrders(): List<OrderDto>

    @PUT("api/Order/admin/cancelorder/{orderId}")
    suspend fun cancelOrder(@Path("orderId") orderId: Int): Response<Unit>

    @POST("api/return")
    suspend fun createReturnRequest(@Body request: CreateReturnRequest): Response<Unit>
}