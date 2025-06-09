package com.gtg.electroshopandroid.data.network;

import com.gtg.electroshopandroid.data.model.CartDto;
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET;
import retrofit2.http.Path
import retrofit2.http.Query

interface CartApiService {
    @GET("api/Cart/user/viewcart")
    suspend fun getCartItems(): List<CartDto>

    @DELETE("api/Cart/user/deletecartitem")
    suspend fun deleteCartItem(
        @Query("productId") productId: Int
    ): Response<Unit>
}
