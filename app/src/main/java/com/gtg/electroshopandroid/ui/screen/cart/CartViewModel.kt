package com.gtg.electroshopandroid.ui.screen.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gtg.electroshopandroid.ElectroShopApplication
import com.gtg.electroshopandroid.data.model.ProductDto
import com.gtg.electroshopandroid.data.repository.ProductRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.gtg.electroshopandroid.data.model.CartDto
import com.gtg.electroshopandroid.data.repository.CartRepository

sealed interface CartUiState {
    object Loading : CartUiState
    data class Success(val cart: List<CartDto>) : CartUiState
    object Error : CartUiState
}

class CartViewModel(
    private val  cartRepository: CartRepository
) : ViewModel() {

    var cartUiState: CartUiState by mutableStateOf(CartUiState.Loading)
        private set

    fun getCartItems() {
        viewModelScope.launch {
            cartUiState = CartUiState.Loading
            cartUiState = try {
                val result = cartRepository.getCartItems()
                CartUiState.Success(result)
            } catch (e: IOException) {
                CartUiState.Error
            } catch (e: HttpException) {
                CartUiState.Error
            }
        }
    }
    fun deleteCartItem(productId: Int) {
        viewModelScope.launch {
            try {
                val response = cartRepository.deleteCartItem(productId)
                if (response.isSuccessful) {
                    // Gọi lại getCartItems để làm mới danh sách
                    getCartItems()
                } else {
                    // Xử lý lỗi nếu cần (ví dụ: log lỗi hoặc cập nhật UI)
                    cartUiState = CartUiState.Error
                }
            } catch (e: IOException) {
                cartUiState = CartUiState.Error
            } catch (e: HttpException) {
                cartUiState = CartUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ElectroShopApplication)
                val cartRepository = application.container.cartRepository
                CartViewModel(cartRepository = cartRepository)
            }
        }
    }
}
