package com.gtg.electroshopandroid.data.repository



import com.gtg.electroshopandroid.data.model.CartDto
import com.gtg.electroshopandroid.data.network.CartApiService
import retrofit2.Response
interface CartRepository {
    suspend fun getCartItems(): List<CartDto>
    suspend fun deleteCartItem(productId: Int): Response<Unit>
    suspend fun deleteCart(): Response<Unit>
    suspend fun addToCart(productId: Int,quantity: Int): CartDto
}

class CartRepositoryImpl(
    private val cartApiService: CartApiService
) : CartRepository {

    override suspend fun getCartItems(): List<CartDto> {
        return cartApiService.getCartItems()
    }
    override suspend fun deleteCartItem(productId: Int): Response<Unit> {
        return cartApiService.deleteCartItem(productId)
    }
    override suspend fun deleteCart(): Response<Unit> {
        return cartApiService.deleteCart()
    }
    override suspend fun addToCart(productId: Int,quantity: Int): CartDto{
        return cartApiService.addToCart(productId,quantity)
    }

}