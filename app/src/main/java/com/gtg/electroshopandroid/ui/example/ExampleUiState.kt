package com.gtg.electroshopandroid.ui.example

import com.gtg.electroshopandroid.data.model.ExampleDto

sealed interface ExampleUiState {
    data class Success(val exampleDto: ExampleDto) : ExampleUiState
    data object Error : ExampleUiState
    data object Loading : ExampleUiState
}