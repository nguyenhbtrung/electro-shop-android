package com.gtg.electroshopandroid.ui.screen.rating

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gtg.electroshopandroid.data.model.RatingDto
import com.gtg.electroshopandroid.data.repository.RatingRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.gtg.electroshopandroid.ElectroShopApplication
import retrofit2.HttpException
import java.io.IOException
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
sealed interface RatingUiState {
    object Loading : RatingUiState
    data class Success(val ratings: List<RatingDto>) : RatingUiState
    object Error : RatingUiState
}
class RatingViewModel(
    private val ratingRepository: RatingRepository
) : ViewModel() {

    var ratingUiState: RatingUiState by mutableStateOf(RatingUiState.Loading)
        private set
    var errorMessage: String? by mutableStateOf(null)
        private set
    fun getRatingsByProductId(productId: Int) {
        viewModelScope.launch {
            ratingUiState = RatingUiState.Loading
            ratingUiState = try {
                val result = ratingRepository.getRatingsByProductId(productId)
                RatingUiState.Success(result)
            } catch (e: IOException) {
                errorMessage = "Network error: ${e.message}"
                RatingUiState.Error
            } catch (e: HttpException) {
                errorMessage = "HTTP error ${e.code()}: ${e.message()}"
                RatingUiState.Error
            } catch (e: Exception) {
                errorMessage = "Unexpected error: ${e.message}"
                RatingUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ElectroShopApplication)
                val ratingRepository = application.container.ratingRepository
                RatingViewModel(ratingRepository = ratingRepository)
            }
        }
    }
}
