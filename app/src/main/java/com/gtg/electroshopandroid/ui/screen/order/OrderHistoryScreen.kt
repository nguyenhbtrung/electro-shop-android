package com.gtg.electroshopandroid.ui.screen.order

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.navigation.NavHostController
import com.gtg.electroshopandroid.convertBaseUrl
import androidx.compose.material3.TextButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextAlign
import com.gtg.electroshopandroid.data.model.Screen
import java.util.Locale
import androidx.compose.material.icons.automirrored.filled.ArrowBack

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
        price = "${String.format(Locale.US, "%,.0f", this.total)}₫",
        quantity = firstOrderItem?.quantity ?: 0,
        status = when(this.status?.lowercase()) {
            "pending" -> "Đang chuẩn bị"
            "successed" -> "Đã hoàn thành"
            "shipping" -> "Đang vận chuyển"
            "cancelled" -> "Đã hủy"
            "return" -> "Trả hàng"
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
        imageUrl = firstOrderItem?.productImage?.let { convertBaseUrl(it) },
        imageRes = com.gtg.electroshopandroid.R.drawable.ic_launcher_foreground
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    navController: NavHostController,
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

    var showCancelDialog by remember { mutableStateOf(false) }
    var orderToCancel by remember { mutableStateOf<Int?>(null) }

    // Tabs
    val tabs = listOf("Tất cả", "Đã hoàn thành", "Đang chuẩn bị", "Đang vận chuyển", "Đã hủy")
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
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

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = {
                showCancelDialog = false
                orderToCancel = null
            },
            title = {
                Text(
                    text = "Xác nhận hủy đơn hàng",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Bạn có chắc chắn muốn hủy đơn hàng này không?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        orderToCancel?.let { orderId ->
                            viewModel.cancelOrder(orderId)
                        }
                        showCancelDialog = false
                        orderToCancel = null
                    }
                ) {
                    Text(
                        text = "Có",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showCancelDialog = false
                        orderToCancel = null
                    }
                ) {
                    Text("Không")
                }
            }
        )
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
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = error!!,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = { viewModel.loadOrders() },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Thử lại")
                        }
                    }
                }
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
                        items(filteredOrders.zip(orders)) { (orderItem, orderDto) ->
                            OrderHistoryCard(
                                order = orderItem,
                                orderDto = orderDto,
                                onDetailClick = {
                                    navController.navigate("order_detail/${orderItem.id}")
                                },
                                onCancelClick = {
                                    orderToCancel = orderItem.id
                                    showCancelDialog = true
                                },
                                onReturnClick = {
                                    navController.navigate(Screen.CreateReturn.route.replace("{orderId}", orderItem.id.toString()))
                                }
                            )
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
                                    if (filteredOrders.isNotEmpty()) {
                                        val totalValue = filteredOrders.sumOf {
                                            it.price.replace("₫", "").replace(",", "").toDoubleOrNull() ?: 0.0
                                        }
                                        Text(
                                            text = "Tổng giá trị: ${String.format(Locale.US, "%,.0f", totalValue)}₫",
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
fun OrderHistoryCard(
    order: OrderItem,
    orderDto: OrderDto,
    onDetailClick: () -> Unit,
    onCancelClick: () -> Unit = {}, // Callback cho nút Hủy
    onReturnClick: () -> Unit = {} // Callback cho nút Trả hàng
) {
    var isExpanded by remember { mutableStateOf(false) }
    val hasMultipleProducts = orderDto.orderItems.size > 1

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onDetailClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = order.imageUrl,
                    contentDescription = order.name,
                    modifier = Modifier
                        .size(80.dp)
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = order.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary,
                            lineHeight = 18.sp,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        if (order.quantity > 0) {
                            Text(
                                text = "x${order.quantity}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = order.price,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(
                    expandFrom = Alignment.Top,
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300)),
                exit = shrinkVertically(
                    shrinkTowards = Alignment.Top,
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            ) {
                Column(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    // Hiển thị các sản phẩm từ index 1 trở đi
                    orderDto.orderItems.drop(1).forEach { item ->
                        AdditionalProductItem(
                            name = item.productName,
                            quantity = item.quantity,
                            imageUrl = item.productImage?.let { convertBaseUrl(it) }
                        )
                    }
                }
            }

            if (hasMultipleProducts) {
                TextButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = if (isExpanded) "Thu gọn" else "Xem thêm ${orderDto.orderItems.size - 1} sản phẩm",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = if (isExpanded)
                            Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = order.status,
                    color = when(order.status) {
                        "Đã hoàn thành" -> MaterialTheme.colorScheme.primary
                        "Đang chuẩn bị" -> MaterialTheme.colorScheme.primary
                        "Đang vận chuyển" -> MaterialTheme.colorScheme.primary
                        "Đã hủy" -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.primary
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                when (order.status) {
                    "Đang chuẩn bị" -> {
                        OutlinedButton(
                            onClick = { onCancelClick() },
                            modifier = Modifier.height(32.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(width = 1.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text = "Hủy",
                                fontSize = 10.sp
                            )
                        }
                    }
                    "Đã hoàn thành" -> {
                        OutlinedButton(
                            onClick = { onReturnClick() },
                            modifier = Modifier.height(32.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(width = 1.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text = "Trả hàng",
                                fontSize = 10.sp
                            )
                        }
                    }
                    else -> {
                        OutlinedButton(
                            onClick = { },
                            enabled = false,
                            modifier = Modifier.height(32.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                            ),
                            border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(width = 1.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text = "Hủy",
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdditionalProductItem(
    name: String,
    quantity: Int,
    imageUrl: String?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = name,
            modifier = Modifier
                .size(50.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(6.dp)
                )
                .padding(6.dp),
            placeholder = painterResource(id = com.gtg.electroshopandroid.R.drawable.ic_launcher_foreground),
            error = painterResource(id = com.gtg.electroshopandroid.R.drawable.ic_launcher_foreground)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f, fill = false)
            )
            Text(
                text = "x$quantity",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
        }
    }
}




