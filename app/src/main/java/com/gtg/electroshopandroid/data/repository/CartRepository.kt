package com.gtg.electroshopandroid.data.repository



import com.gtg.electroshopandroid.data.model.CartDto
import com.gtg.electroshopandroid.data.network.CartApiService

interface CartRepository {
    suspend fun getCartItems(): List<CartDto>
}

class CartRepositoryImpl(
    private val cartApiService: CartApiService
) : CartRepository {

    override suspend fun getCartItems(): List<CartDto> {
        return cartApiService.getCartItems()
    }
}