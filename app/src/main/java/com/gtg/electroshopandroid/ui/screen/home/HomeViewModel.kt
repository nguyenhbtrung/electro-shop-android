package com.gtg.electroshopandroid.ui.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    var searchText by mutableStateOf("")
        private set

    fun onSearchTextChanged(newText: String) {
        searchText = newText
    }
}
