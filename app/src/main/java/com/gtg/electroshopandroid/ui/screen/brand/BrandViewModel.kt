package com.gtg.electroshopandroid.ui.screen.brand

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gtg.electroshopandroid.ElectroShopApplication
import com.gtg.electroshopandroid.data.model.brand.BrandDto
import com.gtg.electroshopandroid.data.model.brand.BrandProductDto
import com.gtg.electroshopandroid.data.repository.BrandRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface ProductByBrandUiState {
    object Loading : ProductByBrandUiState
    data class Success(val products: List<BrandProductDto>) : ProductByBrandUiState
    object Error : ProductByBrandUiState
}
sealed interface BrandUiState {
    object Loading : BrandUiState
    data class Success(val brands: List<BrandDto>) : BrandUiState
    data class Error(val message: String) : BrandUiState
}
class BrandViewModel (
    private val brandRepository: BrandRepository
) : ViewModel(){
    var productByBrandUiState: ProductByBrandUiState by mutableStateOf(ProductByBrandUiState.Loading)
        private set
    var brandUiState: BrandUiState by mutableStateOf(BrandUiState.Loading)
        private set
    var errorMessage: String? by mutableStateOf(null)
        private set
    fun getProductByBrandId(brandId: Int) {
        viewModelScope.launch {
            productByBrandUiState = ProductByBrandUiState.Loading
            productByBrandUiState = try {
                val result = brandRepository.getProductbyBrandId(brandId)
                ProductByBrandUiState.Success(result)
            } catch (e: IOException) {
                errorMessage = "Network error: ${e.message}"
                ProductByBrandUiState.Error
            } catch (e: HttpException) {
                errorMessage = "HTTP error ${e.code()}: ${e.message()}"
                ProductByBrandUiState.Error
            } catch (e: Exception) {
                errorMessage = "Unexpected error: ${e.message}"
                ProductByBrandUiState.Error
            }
        }
    }
    fun getFilteredProducts(brandId: Int, price: Int?, categoryId: Int?, rating: Int?) {
        viewModelScope.launch {
            productByBrandUiState = ProductByBrandUiState.Loading
            try {
                val result = brandRepository.filterProductsByBrand(brandId, price,categoryId , rating)
                productByBrandUiState = ProductByBrandUiState.Success(result)
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
                productByBrandUiState = ProductByBrandUiState.Error
            }
        }
    }
    fun getBrands() {
        viewModelScope.launch {
            brandUiState = BrandUiState.Loading
            try {
                val result = brandRepository.getBrand()
                brandUiState = BrandUiState.Success(result)
            } catch (e: Exception) {
                brandUiState = BrandUiState.Error("Lỗi: ${e.message}")
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ElectroShopApplication)
                val brandRepository = application.container.brandRepository
                BrandViewModel(brandRepository = brandRepository)
            }
        }
    }
}