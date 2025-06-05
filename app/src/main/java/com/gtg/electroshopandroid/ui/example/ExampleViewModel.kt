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

class ExampleViewModel(private val exampleRepository: ExampleRepository) : ViewModel() {
    var exampleUiState: ExampleUiState by mutableStateOf(ExampleUiState.Loading)
        private set

    init {
        getExample()
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ElectroShopApplication)
                val exampleRepository = application.container.exampleRepository
                ExampleViewModel(exampleRepository = exampleRepository)
            }
        }
    }
}