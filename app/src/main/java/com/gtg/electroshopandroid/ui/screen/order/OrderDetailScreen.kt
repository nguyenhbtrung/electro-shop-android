package com.gtg.electroshopandroid.ui.screen.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gtg.electroshopandroid.ElectroShopApplication
import com.gtg.electroshopandroid.data.model.OrderDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderId: Int?,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current.applicationContext as ElectroShopApplication
    val orderHistoryRepository = context.container.orderHistoryRepository

    val viewModel: OrderDetailViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return OrderDetailViewModel(orderHistoryRepository) as T
            }
        }
    )

    val orderDetail by viewModel.orderDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Load dữ liệu khi màn hình được tạo
    LaunchedEffect(orderId) {
        orderId?.let { viewModel.loadOrderDetail(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết đơn hàng") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                orderDetail != null -> {
                    OrderDetailContent(orderDetail = orderDetail!!)
                }
                else -> {
                    Text(
                        text = "Không tìm thấy đơn hàng #$orderId",
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun OrderDetailContent(orderDetail: OrderDto) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Thông tin cơ bản đơn hàng
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Đơn hàng #${orderDetail.orderId}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (orderDetail.fullName?.isNotEmpty() == true) {
                        InfoRow("Khách hàng:", orderDetail.fullName)
                    }

                    if (orderDetail.address?.isNotEmpty() == true) {
                        InfoRow("Địa chỉ:", orderDetail.address)
                    }

                    InfoRow("Tổng tiền:", "${String.format("%,.0f", orderDetail.total)}₫")

                    InfoRow("Trạng thái:", when(orderDetail.status?.lowercase()) {
                        "pending" -> "Đang chuẩn bị"
                        "completed", "successed" -> "Đã hoàn thành"
                        "shipping" -> "Đang vận chuyển"
                        "cancelled" -> "Đã hủy"
                        else -> orderDetail.status ?: "Không xác định"
                    })

                    InfoRow("Trạng thái thanh toán:", when(orderDetail.paymentStatus?.lowercase()) {
                        "paid" -> "Đã thanh toán"
                        "pending" -> "Chờ thanh toán"
                        "refund" -> "Đã hoàn tiền"
                        else -> orderDetail.paymentStatus ?: ""
                    })

                    InfoRow("Phương thức thanh toán:", when(orderDetail.paymentMethod?.lowercase()) {
                        "vnpay" -> "Chuyển khoản"
                        "cod" -> "Tiền mặt"
                        else -> orderDetail.paymentMethod ?: ""
                    })

                    if (orderDetail.timeStamp?.isNotEmpty() == true) {
                        InfoRow("Ngày đặt:", orderDetail.timeStamp.take(10))
                    }
                }
            }
        }

        item {
            // Danh sách sản phẩm
            Text(
                text = "Sản phẩm đã đặt",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(orderDetail.orderItems) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = item.productName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Số lượng: ${item.quantity}")
                        Text(
                            text = "${String.format("%,.0f", item.price)}₫",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
