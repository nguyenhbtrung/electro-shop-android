package com.gtg.electroshopandroid.data.network;

import com.gtg.electroshopandroid.data.model.CartDto;
import retrofit2.http.GET;
import retrofit2.http.Path

interface CartApiService {
    @GET("api/Cart/user/viewcart")
    suspend fun getCartItems(): List<CartDto>
}
