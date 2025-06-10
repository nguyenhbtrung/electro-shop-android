package com.gtg.electroshopandroid.ui.screen.productHistory

import com.gtg.electroshopandroid.data.model.ProductDto


sealed interface ProductHistoryUiState {
    data class Success(val products: List<ProductDto>) : ProductHistoryUiState
    data object Error : ProductHistoryUiState
    data object Loading : ProductHistoryUiState
}
