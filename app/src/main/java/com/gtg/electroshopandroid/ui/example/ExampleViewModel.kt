package com.gtg.electroshopandroid.ui.example

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gtg.electroshopandroid.ElectroShopApplication
import com.gtg.electroshopandroid.data.repository.ExampleRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.gtg.electroshopandroid.data.model.ExampleDto
import com.gtg.electroshopandroid.data.repository.ProductHistoryRepository

class ExampleViewModel(
    private val exampleRepository: ExampleRepository,
    private val productHistoryRepository: ProductHistoryRepository
) : ViewModel() {
    var exampleUiState: ExampleUiState by mutableStateOf(ExampleUiState.Loading)
        private set

    var historyUiState: HistoryUiState by mutableStateOf(HistoryUiState.Loading)
        private set

    init {
        getExample()
        getHistory()
    }

    fun getExample() {
        viewModelScope.launch {
            exampleUiState = ExampleUiState.Loading
            exampleUiState = try {
                ExampleUiState.Success(exampleRepository.getExample())
            } catch (e: IOException) {
                ExampleUiState.Error
            } catch (e: HttpException) {
                ExampleUiState.Error
            }
        }
    }

    fun getHistory() {
        viewModelScope.launch {
            historyUiState = HistoryUiState.Loading
            historyUiState = try {
                HistoryUiState.Success(productHistoryRepository.getHistory())
            } catch (e: IOException) {
                HistoryUiState.Error
            } catch (e: HttpException) {
                HistoryUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ElectroShopApplication)
                val exampleRepository = application.container.exampleRepository
                val productHistoryRepository = application.container.productHistoryRepository
                ExampleViewModel(
                    exampleRepository = exampleRepository,
                    productHistoryRepository = productHistoryRepository
                )
            }
        }
    }
}