package com.gtg.electroshopandroid.ui.screen.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gtg.electroshopandroid.ElectroShopApplication
import com.gtg.electroshopandroid.data.model.FavoriteProductDTO
import com.gtg.electroshopandroid.data.repository.FavoriteRepository
import com.gtg.electroshopandroid.ui.screen.product.RecommendViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<FavoriteProductDTO>>(emptyList())
    val favorites: StateFlow<List<FavoriteProductDTO>> = _favorites

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = favoriteRepository.getFavorites()
                _favorites.value = result
            } catch (e: Exception) {
                Log.e("FavoriteViewModel", "Lỗi loadFavorites(): ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(productId: Int) {
        viewModelScope.launch {
            try {
                val status = favoriteRepository.toggleFavorite(productId)
                if (status == "Removed") {
                    loadFavorites() // refresh lại nếu bị xóa
                }
            } catch (e: Exception) {
                Log.e("FavoriteViewModel", "Lỗi toggleFavorite(): ${e.message}")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ElectroShopApplication)
                val favoriteRepository = application.container.favoriteRepository
                FavoriteViewModel(favoriteRepository = favoriteRepository)
            }
        }
    }
}