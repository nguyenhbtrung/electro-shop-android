package com.gtg.electroshopandroid.ui.screen.product

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

sealed interface ProductUiState {
    object Loading : ProductUiState
    data class Success(val product: ProductDto) : ProductUiState
    object Error : ProductUiState
}

class ProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    var productUiState: ProductUiState by mutableStateOf(ProductUiState.Loading)
        private set

    fun getProductById(id: Int) {
        viewModelScope.launch {
            productUiState = ProductUiState.Loading
            productUiState = try {
                val result = productRepository.getProductById(id)
                ProductUiState.Success(result)
            } catch (e: IOException) {
                ProductUiState.Error
            } catch (e: HttpException) {
                ProductUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ElectroShopApplication)
                val productRepository = application.container.productRepository
                ProductViewModel(productRepository = productRepository)
            }
        }
    }
}
