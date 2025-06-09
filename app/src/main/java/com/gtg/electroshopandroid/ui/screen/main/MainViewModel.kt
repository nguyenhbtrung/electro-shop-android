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
import com.gtg.electroshopandroid.data.repository.CategoryRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val categoryRepository: CategoryRepository
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
            uiState = try {
                uiState.copy(categories = categoryRepository.getCategoryTree())
            } catch (e: Exception) {
                uiState
            }
        }
    }

    init {
        loadCategories()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ElectroShopApplication)
                val categoryRepository = application.container.categoryRepository

                MainViewModel(
                    categoryRepository = categoryRepository,
                )
            }
        }
    }

}