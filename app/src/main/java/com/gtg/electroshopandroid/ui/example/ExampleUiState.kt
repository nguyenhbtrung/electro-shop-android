package com.gtg.electroshopandroid.ui.example

import com.gtg.electroshopandroid.data.model.ExampleDto
import com.gtg.electroshopandroid.data.model.ProductHistoryDto

sealed interface ExampleUiState {
    data class Success(val exampleDto: ExampleDto) : ExampleUiState
    data object Error : ExampleUiState
    data object Loading : ExampleUiState
}

sealed interface HistoryUiState {
    data class Success(val history: List<ProductHistoryDto>) : HistoryUiState
    data object Error : HistoryUiState
    data object Loading : HistoryUiState
}