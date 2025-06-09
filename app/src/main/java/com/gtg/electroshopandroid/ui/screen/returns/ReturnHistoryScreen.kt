package com.gtg.electroshopandroid.ui.screen.returns

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
import com.gtg.electroshopandroid.data.model.ReturnDto
import com.gtg.electroshopandroid.data.repository.ReturnHistoryRepository
import coil.compose.AsyncImage
import androidx.navigation.NavHostController
import com.gtg.electroshopandroid.convertBaseUrl

data class ReturnItem(
    val id: Int,
    val name: String,
    val quantity: Int,
    val status: String,
    val returnMethod: String,
    val returnDate: String,
    val imageUrl: String? = null,
    val imageRes: Int = com.gtg.electroshopandroid.R.drawable.ic_launcher_foreground
)

fun ReturnDto.toReturnItem(): ReturnItem {
    val firstReturnProduct = this.returnProducts.firstOrNull()
    return ReturnItem(
        id = this.returnId,
        name = firstReturnProduct?.name ?: "Sản phẩm hoàn trả",
        quantity = firstReturnProduct?.returnQuantity ?: 0,
        status = when(this.status.lowercase()) {
            "pending" -> "Đang xử lý"
            "approved" -> "Đã duyệt"
            "processing" -> "Đang xử lý"
            "rejected" -> "Từ chối"
            "completed" -> "Hoàn thành"
            "Canceled" -> "Đã hủy"
            else -> this.status
        },
        returnMethod = when(this.returnMethod.lowercase()) {
            "refund" -> "Hoàn tiền"
            "exchange" -> "Đổi hàng"
            "repair" -> "Sửa chữa"
            else -> this.returnMethod
        },
        returnDate = this.timeStamp.take(10),
        imageUrl = firstReturnProduct?.image?.let { convertBaseUrl(it) },
        imageRes = com.gtg.electroshopandroid.R.drawable.ic_launcher_foreground
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReturnHistoryScreen(
    navController: NavHostController,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current.applicationContext as ElectroShopApplication
    val returnHistoryRepository: ReturnHistoryRepository = context.container.returnHistoryRepository

    val viewModel: ReturnHistoryViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ReturnHistoryViewModel(returnHistoryRepository) as T
            }
        }
    )

    // Tabs
    val tabs = listOf("Tất cả", "Đang xử lý", "Đã duyệt", "Từ chối", "Hoàn thành", "Đã hoàn tiền")
    var selectedTabIndex by remember { mutableStateOf(0) }
    val returns by viewModel.returns.collectAsState()
    val allReturns = remember(returns) { returns.map { it.toReturnItem() } }
    var filteredReturns by remember { mutableStateOf<List<ReturnItem>>(emptyList()) }

    // Filter returns theo tab
    LaunchedEffect(selectedTabIndex, allReturns) {
        filteredReturns = when (selectedTabIndex) {
            0 -> allReturns
            1 -> allReturns.filter { it.status == "Đang xử lý" }
            2 -> allReturns.filter { it.status == "Đã duyệt" }
            3 -> allReturns.filter { it.status == "Từ chối" }
            4 -> allReturns.filter { it.status == "Hoàn thành" }
            5 -> allReturns.filter { it.status == "Đã hoàn tiền" }
            else -> allReturns
        }
    }

    // Tải dữ liệu khi mở màn hình
    LaunchedEffect(Unit) {
        viewModel.loadReturns()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBar(
            title = {
                Text(
                    "Lịch sử hoàn trả",
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

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                filteredReturns.isEmpty() && allReturns.isNotEmpty() -> {
                    Text(
                        text = "Không có yêu cầu hoàn trả nào trong danh mục này",
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
                allReturns.isEmpty() -> {
                    Text(
                        text = "Không có yêu cầu hoàn trả nào",
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
                        items(filteredReturns) { returnItem ->
                            ReturnHistoryCard(
                                returnItem = returnItem,
                                onDetailClick = {
                                    navController.navigate("return_detail/${returnItem.id}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReturnHistoryCard(
    returnItem: ReturnItem,
    onDetailClick: () -> Unit = {}
) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = returnItem.imageUrl,
                    contentDescription = returnItem.name,
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp),
                    placeholder = painterResource(id = returnItem.imageRes),
                    error = painterResource(id = returnItem.imageRes)
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
                            text = returnItem.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary,
                            lineHeight = 18.sp,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        if (returnItem.quantity > 0) {
                            Text(
                                text = "x${returnItem.quantity}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Phương thức: ${returnItem.returnMethod}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Ngày: ${returnItem.returnDate}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    text = returnItem.status,
                    color = when(returnItem.status) {
                        "Hoàn thành" -> MaterialTheme.colorScheme.secondary
                        "Đang xử lý" -> MaterialTheme.colorScheme.tertiary
                        "Đã duyệt" -> MaterialTheme.colorScheme.primary
                        "Từ chối" -> MaterialTheme.colorScheme.error
                        "Đã hoàn tiền" -> Color(0xFF4CAF50)
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedButton(
                    onClick = onDetailClick,
                    modifier = Modifier.height(32.dp),
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
            }
        }
    }
}

