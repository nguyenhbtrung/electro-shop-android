package com.gtg.electroshopandroid.ui.screen.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var uiState by mutableStateOf(MainUiState())
        private set

    fun setSelectedCategoryParent(id: Int) {
        uiState = uiState.copy(selectedCategoryParentId = id)
    }

    fun setSelectedCategoryChild(id: Int) {
        uiState = uiState.copy(selectedCategoryChildId = id)
    }

}