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
import com.gtg.electroshopandroid.data.model.CategoryDto
import com.gtg.electroshopandroid.data.model.ProductDto
import com.gtg.electroshopandroid.data.repository.CategoryRepository



sealed interface CategoryUiState {
    object Loading : CategoryUiState
    data class Success(val category: List<CategoryDto>) : CategoryUiState
    object Error : CategoryUiState
}
sealed interface ProductByCategoryUiState {
    object Loading : ProductByCategoryUiState
    data class Success(val products: List<ProductDto>) : ProductByCategoryUiState
    object Error : ProductByCategoryUiState
}
class CategoryViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    var productByCategoryUiState: ProductByCategoryUiState by mutableStateOf(ProductByCategoryUiState.Loading)
        private set
    var categoryUiState: CategoryUiState by mutableStateOf(CategoryUiState.Loading)
        private set
    var errorMessage: String? by mutableStateOf(null)
        private set
    fun getCategoryByProductId(categoryId: Int) {
        viewModelScope.launch {
            categoryUiState = CategoryUiState.Loading
            categoryUiState = try {
                val result = categoryRepository.getCategory(categoryId)
                CategoryUiState.Success(result)
            } catch (e: IOException) {
                errorMessage = "Network error: ${e.message}"
                CategoryUiState.Error
            } catch (e: HttpException) {
                errorMessage = "HTTP error ${e.code()}: ${e.message()}"
                CategoryUiState.Error
            } catch (e: Exception) {
                errorMessage = "Unexpected error: ${e.message}"
                CategoryUiState.Error
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
