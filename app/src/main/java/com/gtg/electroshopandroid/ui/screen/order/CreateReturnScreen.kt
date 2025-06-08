package com.gtg.electroshopandroid.ui.screen.order

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.gtg.electroshopandroid.ElectroShopApplication
import com.gtg.electroshopandroid.R
import com.gtg.electroshopandroid.data.model.*
import com.gtg.electroshopandroid.data.repository.OrderHistoryRepository
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReturnScreen(
    orderId: Int,
    orderDto: OrderDto,
    navController: NavHostController,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current.applicationContext as ElectroShopApplication
    val orderHistoryRepository: OrderHistoryRepository = context.container.orderHistoryRepository

    val viewModel: CreateReturnViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CreateReturnViewModel(orderHistoryRepository) as T
            }
        }
    )

    val formState by viewModel.formState.collectAsState()

    // Initialize form với order data
    LaunchedEffect(orderId) {
        viewModel.initializeForm(orderId, orderDto)
    }

    // Handle submit success
    LaunchedEffect(formState.submitSuccess) {
        if (formState.submitSuccess) {
            navController.navigate(Screen.OrderHistory.route) {
                popUpTo(Screen.CreateReturn.route) { inclusive = true }
            }
        }
    }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.addEvidenceImage(it.toString())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBar(
            title = {
                Text(
                    "Gửi yêu cầu hoàn trả",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Đơn hàng: ${formState.orderId}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Lý do: Sản phẩm lỗi",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = formState.detail,
                    onValueChange = viewModel::updateDetail,
                    label = { Text("Mô tả chi tiết") },
                    placeholder = { Text("Vui lòng mô tả vấn đề gặp phải...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    isError = formState.errors.containsKey("detail"),
                    supportingText = {
                        formState.errors["detail"]?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    }
                )
            }

            item {
                Text(
                    text = "Phương thức xử lý:",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )

                ReturnMethod.entries.forEach { method ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.updateReturnMethod(method.value) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = formState.returnMethod == method.value,
                            onClick = { viewModel.updateReturnMethod(method.value) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = ReturnMethod.fromValue(method.value).displayName,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Chọn sản phẩm cần hoàn trả:",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )

                if (formState.errors.containsKey("returnItems")) {
                    Text(
                        text = formState.errors["returnItems"]!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }

            items(formState.orderItems) { item ->
                ReturnableItemCard(
                    item = item,
                    onToggleSelection = { viewModel.toggleItemSelection(item.orderItemId) },
                    onQuantityChange = { quantity ->
                        viewModel.updateReturnQuantity(item.orderItemId, quantity)
                    }
                )
            }

            // Evidence Images
            item {
                Text(
                    text = "Tải lên hình ảnh minh chứng:",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )

                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Chọn hình ảnh")
                }
            }

            // Display selected images
            if (formState.evidenceImages.isNotEmpty()) {
                items(formState.evidenceImages) { imagePath ->
                    EvidenceImageCard(
                        imagePath = imagePath,
                        onRemove = { viewModel.removeEvidenceImage(imagePath) }
                    )
                }
            }

            item {
                Button(
                    onClick = { viewModel.submitReturnRequest() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = !formState.isLoading
                ) {
                    if (formState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Gửi yêu cầu hoàn trả")
                    }
                }

                formState.errors["submit"]?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ReturnableItemCard(
    item: ReturnableItem,
    onToggleSelection: () -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isSelected) Color(0xFFE3F2FD) else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleSelection() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isSelected,
                onCheckedChange = { onToggleSelection() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            AsyncImage(
                model = item.productImage,
                contentDescription = item.productName,
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                error = painterResource(id = R.drawable.ic_launcher_foreground),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.productName,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
                Text(
                    text = "Tối đa: ${item.maxQuantity}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            if (item.isSelected) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (item.returnQuantity > 1) {
                                onQuantityChange(item.returnQuantity - 1)
                            }
                        }
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Giảm")
                    }

                    Text(
                        text = item.returnQuantity.toString(),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    IconButton(
                        onClick = {
                            if (item.returnQuantity < item.maxQuantity) {
                                onQuantityChange(item.returnQuantity + 1)
                            }
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Tăng")
                    }
                }
            }
        }
    }
}

@Composable
fun EvidenceImageCard(
    imagePath: String,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imagePath,
                contentDescription = "Evidence",
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Hình ảnh minh chứng",
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Xóa",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
