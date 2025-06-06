package com.gtg.electroshopandroid.ui.screen.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gtg.electroshopandroid.ElectroShopApplication
import com.gtg.electroshopandroid.data.model.OrderDto
import com.gtg.electroshopandroid.data.repository.OrderHistoryRepository
import coil.compose.AsyncImage

data class OrderItem(
    val id: Int,
    val name: String,
    val price: String,
    val quantity: Int,
    val status: String,
    val paymentStatus: String,
    val customerName: String,
    val address: String,
    val paymentMethod: String,
    val orderDate: String,
    val imageUrl: String? = null,
    val imageRes: Int = com.gtg.electroshopandroid.R.drawable.ic_launcher_foreground
)

fun OrderDto.toOrderItem(): OrderItem {
    val firstOrderItem = this.orderItems.firstOrNull()
    return OrderItem(
        id = this.orderId,
        name = firstOrderItem?.productName ?: "Đơn hàng trống",
        price = "${String.format("%,.0f", this.total)}₫",
        quantity = firstOrderItem?.quantity ?: 0,
        status = when(this.status?.lowercase()) {
            "pending" -> "Đang chuẩn bị"
            "successed" -> "Đã hoàn thành"
            "shipping" -> "Đang vận chuyển"
            "cancelled" -> "Đã hủy"
            else -> this.status ?: "Không xác định"
        },
        paymentStatus = when(this.paymentStatus?.lowercase()) {
            "paid" -> "Đã thanh toán"
            "pending" -> "Chờ thanh toán"
            "refund" -> "Đã hoàn tiền"
            else -> this.paymentStatus ?: ""
        },
        customerName = this.fullName ?: "Khách hàng",
        address = this.address ?: "",
        paymentMethod = when(this.paymentMethod?.lowercase()) {
            "vnpay" -> "Chuyển khoản"
            "cod" -> "Thanh toán khi nhận hàng"
            else -> this.paymentMethod ?: ""
        },
        orderDate = this.timeStamp?.take(10) ?: "",
        imageUrl = firstOrderItem?.productImage,
        imageRes = com.gtg.electroshopandroid.R.drawable.ic_launcher_foreground
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current.applicationContext as ElectroShopApplication
    val orderHistoryRepository: OrderHistoryRepository = context.container.orderHistoryRepository

    val viewModel: OrderHistoryViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return OrderHistoryViewModel(orderHistoryRepository) as T
            }
        }
    )

    // Tabs
    val tabs = listOf("Tất cả", "Đã hoàn thành", "Đang chuẩn bị", "Đang vận chuyển", "Đã hủy")
    var selectedTabIndex by remember { mutableStateOf(0) }
    val orders by viewModel.orders.collectAsState()
    val allOrders = remember(orders) { orders.map { it.toOrderItem() } }
    var filteredOrders by remember { mutableStateOf<List<OrderItem>>(emptyList()) }

    // Filter orders theo tab
    LaunchedEffect(selectedTabIndex, allOrders) {
        filteredOrders = when (selectedTabIndex) {
            0 -> allOrders
            1 -> allOrders.filter { it.status == "Đã hoàn thành" }
            2 -> allOrders.filter { it.status == "Đang chuẩn bị" }
            3 -> allOrders.filter { it.status == "Đang vận chuyển" }
            4 -> allOrders.filter { it.status == "Đã hủy" }
            else -> allOrders
        }
    }

    // Tải dữ liệu khi mở màn hình
    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBar(
            title = {
                Text(
                    "Lịch sử đơn hàng",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.White,
            contentColor = Color.Blue,
            edgePadding = 16.dp
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTabIndex == index) MaterialTheme.colorScheme.scrim else MaterialTheme.colorScheme.primary,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }
                )
            }
        }

        // Content
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                filteredOrders.isEmpty() && allOrders.isNotEmpty() -> {
                    Text(
                        text = "Không có đơn hàng nào trong danh mục này",
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
                allOrders.isEmpty() -> {
                    Text(
                        text = "Không có đơn hàng nào",
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(top = 16.dp)
                    ) {
                        items(filteredOrders) { order ->
                            OrderHistoryCard(order = order)
                        }
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Thống kê",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Hiển thị: ${filteredOrders.size} / ${allOrders.size} đơn hàng",
                                        color = Color.Gray,
                                        fontSize = 12.sp
                                    )
                                    if (filteredOrders.isNotEmpty()) {
                                        val totalValue = filteredOrders.sumOf {
                                            it.price.replace("₫", "").replace(",", "").toDoubleOrNull() ?: 0.0
                                        }
                                        Text(
                                            text = "Tổng giá trị: ${String.format("%,.0f", totalValue)}₫",
                                            color = Color.Blue,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderHistoryCard(order: OrderItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Nội dung chính: ảnh, tên sản phẩm, số lượng, giá
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = order.imageUrl,
                    contentDescription = order.name,
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp),
                    placeholder = painterResource(id = order.imageRes),
                    error = painterResource(id = order.imageRes)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = order.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (order.quantity > 0) {
                        Text(
                            text = "Số lượng: x${order.quantity}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    Text(
                        text = order.price,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Hàng dưới cùng: Trạng thái đơn hàng (trái) và các nút (phải)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Trạng thái đơn hàng ở góc trái dưới
                Text(
                    text = order.status,
                    color = when(order.status) {
                        "Đã hoàn thành" -> MaterialTheme.colorScheme.secondary
                        "Đang chuẩn bị" -> MaterialTheme.colorScheme.tertiary
                        "Đang vận chuyển" -> MaterialTheme.colorScheme.primary
                        "Đã hủy" -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                // Các nút Chi tiết và Đánh giá
                Row {
                    OutlinedButton(
                        onClick = { /* Xử lý xem chi tiết */ },
                        modifier = Modifier
                            .height(32.dp)
                            .padding(end = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                    ) {
                        Text(
                            text = "Chi tiết",
                            fontSize = 10.sp
                        )
                    }
                    OutlinedButton(
                        onClick = { /* Xử lý đánh giá */ },
                        modifier = Modifier.height(32.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.secondary
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                    ) {
                        Text(
                            text = "Đánh giá",
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}



