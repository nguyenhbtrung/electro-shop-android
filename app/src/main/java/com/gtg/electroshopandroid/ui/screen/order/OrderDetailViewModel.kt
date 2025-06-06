package com.gtg.electroshopandroid.ui.screen.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtg.electroshopandroid.data.model.OrderDto
import com.gtg.electroshopandroid.data.repository.OrderHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderDetailViewModel(
    private val orderHistoryRepository: OrderHistoryRepository
) : ViewModel() {

    private val _orderDetail = MutableStateFlow<OrderDto?>(null)
    val orderDetail: StateFlow<OrderDto?> = _orderDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadOrderDetail(orderId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val allOrders = orderHistoryRepository.getAllOrders()
                val foundOrder = allOrders.find { it.orderId == orderId }
                _orderDetail.value = foundOrder
            } catch (e: Exception) {
                _orderDetail.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}
