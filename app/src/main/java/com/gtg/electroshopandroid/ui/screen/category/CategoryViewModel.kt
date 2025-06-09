package com.gtg.electroshopandroid.ui.screen.category

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.gtg.electroshopandroid.ElectroShopApplication
import retrofit2.HttpException
import java.io.IOException
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.gtg.electroshopandroid.data.model.category.CategoryDto
import com.gtg.electroshopandroid.data.model.category.CategoryProductDto
import com.gtg.electroshopandroid.data.repository.CategoryRepository



sealed interface CategoryUiState {
    object Loading : CategoryUiState
    data class Success(val category: List<CategoryDto>) : CategoryUiState
    object Error : CategoryUiState
}
sealed interface ProductByCategoryUiState {
    object Loading : ProductByCategoryUiState
    data class Success(val products: List<CategoryProductDto>) : ProductByCategoryUiState
    object Error : ProductByCategoryUiState
}
class CategoryViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    var productByCategoryUiState: ProductByCategoryUiState by mutableStateOf(ProductByCategoryUiState.Loading)
        private set
    var errorMessage: String? by mutableStateOf(null)
        private set
    fun getFilteredProducts(categoryId: Int, price: Int?, brandId: Int?, rating: Int?) {
        viewModelScope.launch {
            productByCategoryUiState = ProductByCategoryUiState.Loading
            try {
                val result = categoryRepository.filterProductsByCategory(categoryId, price, brandId, rating)
                productByCategoryUiState = ProductByCategoryUiState.Success(result)
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
                productByCategoryUiState = ProductByCategoryUiState.Error
            }
        }
    }
    fun getProductByCategoryId(categoryId: Int) {
        viewModelScope.launch {
            productByCategoryUiState = ProductByCategoryUiState.Loading
            productByCategoryUiState = try {
                val result = categoryRepository.getProductbyCategoryId(categoryId)
                ProductByCategoryUiState.Success(result)
            } catch (e: IOException) {
                errorMessage = "Network error: ${e.message}"
                ProductByCategoryUiState.Error
            } catch (e: HttpException) {
                errorMessage = "HTTP error ${e.code()}: ${e.message()}"
                ProductByCategoryUiState.Error
            } catch (e: Exception) {
                errorMessage = "Unexpected error: ${e.message}"
                ProductByCategoryUiState.Error
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ElectroShopApplication)
                val categoryRepository = application.container.categoryRepository
                CategoryViewModel(categoryRepository = categoryRepository)
            }
        }
    }
}
