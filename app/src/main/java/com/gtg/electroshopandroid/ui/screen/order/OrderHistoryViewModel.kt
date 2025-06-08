package com.gtg.electroshopandroid.ui.screen.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtg.electroshopandroid.data.model.OrderDto
import com.gtg.electroshopandroid.data.repository.OrderHistoryRepository
import com.gtg.electroshopandroid.data.model.CreateReturnRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderHistoryViewModel(
    private val orderHistoryRepository: OrderHistoryRepository
) : ViewModel() {

    private val _orders = MutableStateFlow<List<OrderDto>>(emptyList())
    val orders: StateFlow<List<OrderDto>> = _orders

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val orderList = orderHistoryRepository.getAllOrders()
                _orders.value = orderList
            } catch (e: Exception) {
                _error.value = e.message ?: "Có lỗi xảy ra khi tải đơn hàng"
                _orders.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Thêm function hủy đơn hàng
    fun cancelOrder(orderId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = orderHistoryRepository.cancelOrder(orderId)
                if (success) {
                    // Refresh danh sách đơn hàng sau khi hủy thành công
                    loadOrders()
                } else {
                    _error.value = "Không thể hủy đơn hàng"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Có lỗi xảy ra khi hủy đơn hàng"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createReturnRequest(request: CreateReturnRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = orderHistoryRepository.createReturnRequest(request)
                if (success) {
                    _error.value = "Tạo yêu cầu hoàn trả thành công!"
                    // Refresh danh sách đơn hàng
                    loadOrders()
                } else {
                    _error.value = "Không thể tạo yêu cầu hoàn trả"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Có lỗi xảy ra khi tạo yêu cầu hoàn trả"
            } finally {
                _isLoading.value = false
            }
        }
    }


}

