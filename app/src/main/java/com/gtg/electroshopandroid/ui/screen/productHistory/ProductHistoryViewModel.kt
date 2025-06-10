package com.gtg.electroshopandroid.ui.screen.productHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gtg.electroshopandroid.ElectroShopApplication
import com.gtg.electroshopandroid.data.repository.ProductHistoryRepository
import com.gtg.electroshopandroid.data.repository.ProductRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductHistoryViewModel (
    private val productHistoryRepository: ProductHistoryRepository,
    private val productRepository: ProductRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<ProductHistoryUiState>(ProductHistoryUiState.Loading)
    val uiState: StateFlow<ProductHistoryUiState> = _uiState.asStateFlow()

    fun loadHistory() {
        viewModelScope.launch {
            _uiState.value = ProductHistoryUiState.Loading
            try {
                // 2. Lấy danh sách lịch sử
                val historyList = productHistoryRepository.getHistory()

                // 3. Với mỗi historyDto, khởi chạy async để gọi song song getProductById
                val products = historyList.map { historyDto ->
                    async {
                        productRepository.getProductById(historyDto.productId)
                    }
                }
                    // 4. Chờ tất cả hoàn thành và thu về List<ProductDto>
                    .awaitAll()

                // 5. Đẩy dữ liệu thành công về UI
                _uiState.value = ProductHistoryUiState.Success(products)
            } catch (e: Exception) {
                // 6. Xử lý lỗi chung
                _uiState.value = ProductHistoryUiState.Error
                // optional: Log.e("ProductHistoryVM", "loadHistory failed", e)
            }
        }
    }

    fun deleteHistory(productId: Int) {
        viewModelScope.launch {
            // Bạn có thể show loading indicator riêng cho delete nếu muốn
            try {
                // Gọi API xóa
                productHistoryRepository.deleteHistory(productId)
                loadHistory()
            } catch (e: Exception) {

                loadHistory()
            }
        }
    }



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ElectroShopApplication)
                val productHistoryRepository = application.container.productHistoryRepository
                val productRepository = application.container.productRepository
                ProductHistoryViewModel(
                    productHistoryRepository = productHistoryRepository,
                    productRepository = productRepository
                )
            }
        }
    }
}