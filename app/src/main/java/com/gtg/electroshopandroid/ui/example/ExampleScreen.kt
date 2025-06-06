package com.gtg.electroshopandroid.ui.example

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ExampleScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        Surface(
            modifier = Modifier.fillMaxSize().padding(it)
        ) {
            val exampleViewModel: ExampleViewModel =
                viewModel(factory = ExampleViewModel.Factory)
            Column {
                when (val exampleUiState = exampleViewModel.exampleUiState) {
                    is ExampleUiState.Loading -> Text("Loading")
                    is ExampleUiState.Success -> Text(exampleUiState.exampleDto.message)
                    is ExampleUiState.Error -> Text("Error")
                }
                when (val historyUiState = exampleViewModel.historyUiState) {
                    is HistoryUiState.Loading -> Text("Loading")
                    is HistoryUiState.Success -> Text(historyUiState.history.toString())
                    is HistoryUiState.Error -> Text("Error")
                }
            }
        }
    }
}