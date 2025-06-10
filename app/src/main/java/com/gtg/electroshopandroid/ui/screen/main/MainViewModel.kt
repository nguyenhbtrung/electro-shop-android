package com.gtg.electroshopandroid.ui.screen.main

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
import com.gtg.electroshopandroid.data.repository.BrandRepository
import com.gtg.electroshopandroid.data.repository.CategoryRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val categoryRepository: CategoryRepository,
    private val brandRepository: BrandRepository
) : ViewModel() {
    var uiState by mutableStateOf(MainUiState())
        private set

    fun setSelectedCategoryParent(id: Int) {
        uiState = uiState.copy(selectedCategoryParentId = id)
    }

    fun setSelectedCategoryChild(id: Int) {
        uiState = uiState.copy(selectedCategoryChildId = id)
    }

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val categories = categoryRepository.getCategoryTree()
                uiState = uiState.copy(categories = categories)
            } catch (e: Exception) {
                // Xử lý lỗi nếu cần
            }
        }
    }

    fun setSelectedBrand(id: Int) {
        uiState = uiState.copy(selectedBrandId = id)
    }

    fun loadBrands() {
        viewModelScope.launch {
            try {
                val brands = brandRepository.getBrand()
                uiState = uiState.copy(brands = brands)
            } catch (e: Exception) {
                // Xử lý lỗi nếu cần
            }
        }
    }

    init {
        loadCategories()
        loadBrands()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ElectroShopApplication)
                val categoryRepository = application.container.categoryRepository
                val brandRepository = application.container.brandRepository
                MainViewModel(
                    categoryRepository = categoryRepository,
                    brandRepository =brandRepository
                )
            }
        }
    }

}