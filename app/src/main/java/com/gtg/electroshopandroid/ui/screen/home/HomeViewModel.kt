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
import com.gtg.electroshopandroid.data.repository.FavoriteRepository
import com.gtg.electroshopandroid.data.repository.ProductRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow

class HomeViewModel(
    private val bannerRepository: BannerRepository,
    private val productRepository: ProductRepository,
    private val _searchUiState: MutableStateFlow<SearchUiState> =
        MutableStateFlow(SearchUiState.Loading),
    val searchUiState: StateFlow<SearchUiState> = _searchUiState


    private val favoriteRepository: FavoriteRepository
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

    fun LoadDiscountedProducts() {
        viewModelScope.launch {
            uiState = uiState.copy(discountedProducts = initLoadingProducts())
            val products = try {
                productRepository.getDiscountedProducts()
            } catch (e: Exception) {
                uiState.discountedProducts
            }

            uiState = uiState.copy(discountedProducts = products)
        }
    }
    fun searchProductsByName(productName: String) {
        viewModelScope.launch {
            _searchUiState.value = SearchUiState.Loading
            try {
                val result = productRepository.getProductsSearch(productName)
                _searchUiState.value = SearchUiState.Success(result)
            } catch (e: IOException) {
                _searchUiState.value = SearchUiState.Error
            } catch (e: HttpException) {
                _searchUiState.value = SearchUiState.Error
            } catch (e: Exception) {
                Log.e("SearchError", e.message ?: "")
                _searchUiState.value = SearchUiState.Error
            }
        }
    }

    fun LoadBestSellerProducts() {
        viewModelScope.launch {
            uiState = uiState.copy(bestSellerProducts = initLoadingProducts())
            val products = try {
                productRepository.getBestSellerProducts()
            } catch (e: Exception) {
                uiState.bestSellerProducts
            }

            uiState = uiState.copy(bestSellerProducts = products)
        }
    }

    private suspend fun toggleFavoriteSuspend(productId: Int): Boolean =
        try {
            favoriteRepository.toggleFavorite(productId) == "Added"
        } catch (e: Exception) {
            false
        }

    fun onToggleFavorite(productId: Int) {
        viewModelScope.launch {
            val newIsFav = toggleFavoriteSuspend(productId)
            uiState = uiState.copy(
                discountedProducts = uiState.discountedProducts
                    .map { if (it.productId == productId) it.copy(isFavorite = newIsFav) else it },
                bestSellerProducts = uiState.bestSellerProducts
                    .map { if (it.productId == productId) it.copy(isFavorite = newIsFav) else it },
            )
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
                val favoriteRepository = application.container.favoriteRepository

                HomeViewModel(
                    bannerRepository = bannerRepository,
                    productRepository = productRepository,
                    favoriteRepository = favoriteRepository
                )
            }
        }
    }
}
