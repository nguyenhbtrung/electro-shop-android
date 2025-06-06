package com.gtg.electroshopandroid.ui.screen.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtg.electroshopandroid.data.model.OrderDto
import com.gtg.electroshopandroid.data.repository.OrderHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderHistoryViewModel(
    private val orderHistoryRepository: OrderHistoryRepository
) : ViewModel() {

    private val _orders = MutableStateFlow<List<OrderDto>>(emptyList())
    val orders: StateFlow<List<OrderDto>> = _orders

    fun loadOrders() {
        viewModelScope.launch {
            _orders.value = orderHistoryRepository.getAllOrders()
        }
    }
}
