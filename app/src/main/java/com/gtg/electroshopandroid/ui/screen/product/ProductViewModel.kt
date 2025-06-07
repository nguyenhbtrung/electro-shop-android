package com.gtg.electroshopandroid.ui.screen.product

import android.util.Log
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
import com.gtg.electroshopandroid.data.model.product.RecommendDto
import com.gtg.electroshopandroid.data.repository.RecommendRepository
import kotlin.math.log

sealed interface ProductUiState {
    object Loading : ProductUiState
    data class Success(val product: ProductDto) : ProductUiState
    object Error : ProductUiState
}
sealed interface RecommendUiState {
    object Loading : RecommendUiState
    data class Success(val recommendations: List<RecommendDto>) : RecommendUiState
    object Error : RecommendUiState
}

class ProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    var productUiState: ProductUiState by mutableStateOf(ProductUiState.Loading)
        private set

    fun getProductById(id: Int) {
        viewModelScope.launch {
            productUiState = ProductUiState.Loading
            Log.d("Check product", id.toString())
            productUiState = try {
                val result = productRepository.getProductById(id)
                Log.d("Check product", result.toString())
                ProductUiState.Success(result)
            } catch (e: IOException) {

                ProductUiState.Error
            } catch (e: HttpException) {
                ProductUiState.Error
            } catch (e: Exception) {
                Log.e("Check product", e.message ?: "")
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
class RecommendViewModel(
    private val recommendRepository: RecommendRepository
) : ViewModel() {

    var recommendUiState: RecommendUiState by mutableStateOf(RecommendUiState.Loading)
        private set

    fun getRecommendations(productId: Int) {
        viewModelScope.launch {
            recommendUiState = RecommendUiState.Loading
            try {
                val data: List<RecommendDto> = recommendRepository.getRecommendById(productId)
                recommendUiState = RecommendUiState.Success(data)
            } catch (e: IOException) {
                recommendUiState = RecommendUiState.Error
            } catch (e: HttpException) {
                recommendUiState = RecommendUiState.Error
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ElectroShopApplication)
                val recommendRepository = application.container.recommendRepository
                RecommendViewModel(recommendRepository = recommendRepository)
            }
        }
    }
}

