package com.gtg.electroshopandroid.ui.screen.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gtg.electroshopandroid.ElectroShopApplication
import com.gtg.electroshopandroid.data.model.OrderDto
import com.gtg.electroshopandroid.data.repository.OrderHistoryRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen() {
    val context = LocalContext.current.applicationContext as ElectroShopApplication
    val orderHistoryRepository: OrderHistoryRepository = context.container.orderHistoryRepository

    // Tạo ViewModel sử dụng repository từ AppContainer
    val viewModel: OrderHistoryViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return OrderHistoryViewModel(orderHistoryRepository) as T
            }
        }
    )

    val orders by viewModel.orders.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lịch sử đơn hàng") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(orders) { order ->
                OrderItemCard(order)
            }
        }
    }
}

@Composable
fun OrderItemCard(order: OrderDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = "Mã đơn: ${order.orderId}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Khách hàng: ${order.fullName ?: ""}")
            Text(text = "Tổng tiền: ${order.total}")
            Text(text = "Trạng thái: ${order.status ?: ""}")
            Text(text = "Ngày đặt: ${order.timeStamp ?: ""}")
        }
    }
}
