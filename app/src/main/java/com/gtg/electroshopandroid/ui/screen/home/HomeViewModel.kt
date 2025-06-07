package com.gtg.electroshopandroid.ui.screen.home

import android.util.Log
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
import com.gtg.electroshopandroid.data.repository.BannerRepository
import com.gtg.electroshopandroid.data.repository.ProductRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class HomeViewModel(
    private val bannerRepository: BannerRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    var searchText by mutableStateOf("")
        private set

    fun onSearchTextChanged(newText: String) {
        searchText = newText
    }

    fun LoadBanner() {
        viewModelScope.launch {
            uiState = uiState.copy(banners = initLoadingBanners())
            val banners = try {
                bannerRepository.getBanners()
            } catch (e: Exception) {
                uiState.banners
            }

            uiState = uiState.copy(banners = banners)
        }
    }

    init {
        LoadBanner()
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ElectroShopApplication)
                val bannerRepository = application.container.bannerRepository
                val productRepository = application.container.productRepository

                HomeViewModel(
                    bannerRepository = bannerRepository,
                    productRepository = productRepository
                )
            }
        }
    }
}
