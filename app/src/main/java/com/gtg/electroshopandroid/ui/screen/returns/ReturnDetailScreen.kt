package com.gtg.electroshopandroid.ui.screen.returns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.gtg.electroshopandroid.ElectroShopApplication
import com.gtg.electroshopandroid.data.model.ReturnDetailDto
import com.gtg.electroshopandroid.data.repository.ReturnHistoryRepository
import com.gtg.electroshopandroid.convertBaseUrl
import com.gtg.electroshopandroid.data.model.getStatusDisplayName
import com.gtg.electroshopandroid.data.model.getReturnMethodDisplayName
import com.gtg.electroshopandroid.data.model.getFormattedDate
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.text.style.TextAlign
import com.gtg.electroshopandroid.data.model.WorkflowPath

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReturnDetailScreen(
    returnId: Int,
    navController: NavHostController,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current.applicationContext as ElectroShopApplication
    val returnHistoryRepository: ReturnHistoryRepository = context.container.returnHistoryRepository

    val viewModel: ReturnDetailViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ReturnDetailViewModel(returnHistoryRepository) as T
            }
        }
    )

    val returnDetail by viewModel.returnDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(returnId) {
        viewModel.loadReturnDetail(returnId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết hoàn trả") },
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
                error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = error!!,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(
                            onClick = { viewModel.loadReturnDetail(returnId) },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Thử lại")
                        }
                    }
                }
                returnDetail != null -> {
                    ReturnDetailContent(returnDetail = returnDetail!!, navController = navController)
                }
                else -> {
                    Text(
                        text = "Không tìm thấy thông tin hoàn trả",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

data class ProgressStep(
    val status: String,
    val title: String,
    val date: String
)

@Composable
fun HorizontalProgressStepper(
    currentStatus: String,
    createdDate: String,
    modifier: Modifier = Modifier
) {
    val workflowPath = when(currentStatus.lowercase()) {
        "pending" -> WorkflowPath.PENDING
        "approved" -> WorkflowPath.APPROVED
        "processing" -> WorkflowPath.PROCESSING
        "rejected" -> WorkflowPath.REJECTED
        "completed" -> WorkflowPath.COMPLETED
        "canceled" -> WorkflowPath.CANCELED
        else -> WorkflowPath.PENDING
    }

    val steps = when {
        workflowPath == WorkflowPath.REJECTED || workflowPath == WorkflowPath.CANCELED -> {
            // Path tiêu cực: Pending -> Rejected -> Canceled
            listOf(
                ProgressStep(
                    "pending",
                    "Chờ xử lý",
                    if (workflowPath != WorkflowPath.PENDING) createdDate.take(10) else ""
                ),
                ProgressStep("rejected", "Từ chối", ""),
                ProgressStep("canceled", "Đã hủy", ""),
                ProgressStep("", "", "") // Empty step để giữ 4 chặng
            )
        }

        else -> {
            // Path tích cực: Pending -> Approved -> Processing -> Completed
            listOf(
                ProgressStep(
                    "pending",
                    "Chờ xử lý",
                    if (workflowPath != WorkflowPath.PENDING) createdDate.take(10) else ""
                ),
                ProgressStep("approved", "Đã duyệt", ""),
                ProgressStep("processing", "Đang xử lý", ""),
                ProgressStep("completed", "Hoàn thành", "")
            )
        }
    }

    val currentStepIndex = when(workflowPath) {
        WorkflowPath.PENDING -> 0
        WorkflowPath.APPROVED -> 1
        WorkflowPath.PROCESSING -> 2
        WorkflowPath.COMPLETED -> 3
        WorkflowPath.REJECTED -> 1
        WorkflowPath.CANCELED -> 2
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Tiến trình",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Progress steps
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top
            ) {
                steps.forEachIndexed { index, step ->
                    if (step.status.isNotEmpty()) { // Chỉ hiển thị step không rỗng
                        HorizontalStepItem(
                            step = step,
                            isCompleted = index < currentStepIndex,
                            isCurrent = index == currentStepIndex,
                            isUpcoming = index > currentStepIndex,
                            isRejected = workflowPath == WorkflowPath.REJECTED || workflowPath == WorkflowPath.CANCELED,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (index < steps.size - 1) {
                        Box(
                            modifier = Modifier
                                .weight(0.6f)
                                .padding(top = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            HorizontalStepConnector(
                                isCompleted = index < currentStepIndex
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HorizontalStepItem(
    step: ProgressStep,
    isCompleted: Boolean,
    isCurrent: Boolean,
    isUpcoming: Boolean,
    isRejected: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Circle indicator
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = when {
                        isCompleted || isCurrent -> {
                            if (isRejected) Color(0xFFE53E3E) // Đỏ cho rejected path
                            else Color(0xFFFF9800) // Cam cho approved path
                        }
                        isUpcoming -> Color(0xFFE0E0E0)
                        else -> Color.Gray
                    },
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            when {
                isCompleted -> {
                    if (isRejected && (step.status == "rejected" || step.status == "canceled")) {
                        Icon(
                            imageVector = Icons.Default.Close, // X icon cho rejected
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                isCurrent -> {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                color = Color.White,
                                shape = CircleShape
                            )
                    )
                }
                isUpcoming -> {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                color = Color(0xFFBDBDBD), // Xám đậm hơn cho upcoming
                                shape = CircleShape
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Step title
        Text(
            text = step.title,
            fontSize = 12.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
            color = when {
                isCompleted || isCurrent -> {
                    if (isRejected) Color(0xFFE53E3E) // Đỏ cho rejected
                    else Color.Black
                }
                isUpcoming -> Color(0xFF9E9E9E)
                else -> Color.Gray
            },
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )

        // Date (chỉ hiển thị cho current step)
        if (isCurrent && step.date.isNotEmpty()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = step.date,
                fontSize = 10.sp,
                color = Color(0xFF757575),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun HorizontalStepConnector(
    isCompleted: Boolean,
    isRejected: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(2.dp)
            .background(
                color = when {
                    isCompleted && isRejected -> Color(0xFFE53E3E) // Đỏ cho rejected path
                    isCompleted -> Color(0xFFFF9800) // Cam cho approved path
                    else -> Color(0xFFE0E0E0) // Xám cho chưa hoàn thành
                }
            )
    )
}

@Composable
fun InfoRowVertical(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
fun ReturnDetailContent(
    returnDetail: ReturnDetailDto,
    navController: NavHostController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Yêu cầu hoàn trả #${returnDetail.returnId}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    InfoRow("Đơn hàng:", "#${returnDetail.orderId}")
                    InfoRow("Lý do:", returnDetail.reason)
                    InfoRow("Mô tả:", returnDetail.detail ?: "Không có")
                    InfoRow("Trạng thái:", returnDetail.getStatusDisplayName())
                    InfoRow("Phương thức:", returnDetail.getReturnMethodDisplayName())
                    InfoRow("Ngày tạo:", returnDetail.getFormattedDate())

                    if (returnDetail.adminComment?.isNotEmpty() == true) {
                        InfoRowVertical("Ghi chú admin:", returnDetail.adminComment)
                    }
                }
            }
        }

        item {
            HorizontalProgressStepper(
                currentStatus = returnDetail.status,
                createdDate = returnDetail.createdAt
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Sản phẩm hoàn trả",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    returnDetail.returnProducts.forEach { product ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = convertBaseUrl(product.image),
                                contentDescription = product.name,
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(
                                        MaterialTheme.colorScheme.surfaceVariant,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(6.dp),
                                placeholder = painterResource(id = com.gtg.electroshopandroid.R.drawable.ic_launcher_foreground),
                                error = painterResource(id = com.gtg.electroshopandroid.R.drawable.ic_launcher_foreground)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = product.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Số lượng: ${product.returnQuantity}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
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
