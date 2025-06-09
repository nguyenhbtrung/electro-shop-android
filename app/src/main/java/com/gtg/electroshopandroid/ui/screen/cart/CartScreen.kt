package com.gtg.electroshopandroid.ui.screen.cart
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.gtg.electroshopandroid.R // Thay thế bằng package của bạn, ví dụ: com.yourcompany.yourapp.R
import com.gtg.electroshopandroid.data.model.CartDto
import com.gtg.electroshopandroid.ui.screen.payment.CheckoutActivity
import com.gtg.electroshopandroid.ui.screen.product.ProductUiState
import com.gtg.electroshopandroid.ui.screen.product.ProductViewModel
//import com.gtg.electroshopandroid.ui.screen.product.toAndroidAccessibleUrl
import java.text.NumberFormat
import java.util.Locale

fun String.toAndroidAccessibleUrl(): String {
    return this.replace(Regex("https://localhost(:\\d+)?"), "http://10.0.2.2:5030")
}
fun formatPrice(price: Double): String {
    val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
    return formatter.format(price)
}
@Composable
fun CartScreen() {
    val viewModel: CartViewModel = viewModel(factory = CartViewModel.Factory)
    val cartUiState = viewModel.cartUiState

    // Từng trạng thái được lưu theo productId
    val checkedMap = remember { mutableStateMapOf<Int, Boolean>() }
    val quantityMap = remember { mutableStateMapOf<Int, Int>() }

    LaunchedEffect(Unit) {
        viewModel.getCartItems()
    }

    when (cartUiState) {
        is CartUiState.Loading -> Text("Đang tải sản phẩm...")
        is CartUiState.Error -> Text("Không thể tải sản phẩm.")
        is CartUiState.Success -> {
            val cart = (cartUiState as CartUiState.Success).cart

            // Tính tổng giá
            val totalPrice = cart.sumOf { item ->
                val isChecked = checkedMap[item.productId] ?: true
                val quantity = quantityMap[item.productId] ?: item.quantity
                if (isChecked) item.price.toInt() * quantity else 0
            }

            // Kiểm tra xem còn item nào được check không
            val anyItemChecked = cart.any { item ->
                checkedMap[item.productId] ?: true
            }

            Column(
                modifier = Modifier.fillMaxSize().background(Color(0xFFE0E0E0))

            ) {
                CartHeader()

                LazyColumn(
                    modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(cart, key = { it.productId }) { cartItem ->
                        val quantity = quantityMap[cartItem.productId] ?: cartItem.quantity
                        val isChecked = checkedMap[cartItem.productId] ?: true

                        CartItemCard(
                            viewModel = viewModel,
                            cart = cartItem,
                            quantity = quantity,
                            onQuantityChange = {
                                quantityMap[cartItem.productId] = it
                            },
                            isChecked = isChecked,
                            onCheckedChange = {
                                checkedMap[cartItem.productId] = it
                            },
                            selectedRam = "16GB", // Có thể map riêng nếu cần
                            onRamSelected = { /* TODO */ },
                            selectedSsd = "256GB", // Có thể map riêng nếu cần
                            onSsdSelected = { /* TODO */ }
                        )
                    }
                }

                if (anyItemChecked) {
                    CartBottomBar(totalPrice = totalPrice)
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
@Composable
fun CartHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding( horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = { /* TODO */ }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
        }

        Text(
            text = "Giỏ hàng",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.width(48.dp))
    }
}
@Composable
fun CartItemCard(
    viewModel: CartViewModel,
    cart: CartDto,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    selectedRam: String,
    onRamSelected: (String) -> Unit,
    selectedSsd: String,
    onSsdSelected: (String) -> Unit
) {
    val imageUrls = cart.productImage.toAndroidAccessibleUrl()
    val textpc=cart.productName
    val pricepc=cart.price

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .align(Alignment.Top)
                    .offset(x = 2.dp, y = (-8).dp)
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = onCheckedChange,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape)
                        .background(Color.White),
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Red,
                        uncheckedColor = Color.White,
                        checkmarkColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.width(8.dp))


            // thay URL thật vào đây. img painter = painterResource(R.drawable.logo),
            AsyncImage(
                model = imageUrls, // thay URL thật vào đây
                contentDescription = "Ảnh sản phẩm PC FASTER GAMING",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            //mô tả
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = textpc,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 2
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Đánh giá", tint = Color.Yellow, modifier = Modifier.size(16.dp))
                    Text(text = "4.8", fontSize = 14.sp)
                }

                Text(text = formatPrice(pricepc), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))

                // Lựa chọn RAM
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("RAM:")
                    Spacer(Modifier.width(4.dp))
                    listOf("16GB", "32GB").forEach { option ->
                        OutlinedButton(
                            onClick = { onRamSelected(option) },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (selectedRam == option) Color(0xFFFFF0F0) else Color.Transparent
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (selectedRam == option) Color.Red else Color.LightGray
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .padding(end = 4.dp)
                        ) {
                            Text(option, color = if (selectedRam == option) Color.Red else Color.Black)
                        }
                    }
                }

                // Lựa chọn SSD
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("SSD:")
                    Spacer(Modifier.width(4.dp))
                    listOf("256GB", "500GB").forEach { option ->
                        OutlinedButton(
                            onClick = { onSsdSelected(option) },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (selectedSsd == option) Color(0xFFFFF0F0) else Color.Transparent
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (selectedSsd == option) Color.Red else Color.LightGray
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .padding(end = 4.dp)
                        ) {
                            Text(option, color = if (selectedSsd == option) Color.Red else Color.Black)
                        }
                    }
                }

                // Bộ điều chỉnh số lượng
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    IconButton(
                        onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Giảm số lượng", tint = Color.Black)
                    }

                    Text(
                        text = quantity.toString(),
                        modifier = Modifier.width(36.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    IconButton(
                        onClick = { onQuantityChange(quantity + 1) },
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Tăng số lượng", tint = Color.Black)
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    // 🗑 Nút xoá sản phẩm
                    IconButton(
                        onClick = {
                            viewModel.deleteCartItem(cart.productId)
                        },
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFFFF0F0))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Xoá sản phẩm",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun CartBottomBar(totalPrice: Int) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Tổng tiền:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "%,d đ".format(totalPrice),
                fontSize = 18.sp,
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(context, CheckoutActivity::class.java)
                intent.putExtra("amount", totalPrice.toDouble())

                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("THANH TOÁN", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}
@Preview(showBackground = true, widthDp = 360, heightDp = 640) // Kích thước preview gần với màn hình điện thoại
@Composable
fun CartScreenPreview() {
    // Đảm bảo rằng R.drawable.sample_pc đã được định nghĩa trong dự án của bạn
    // Để preview hoạt động, bạn có thể cần một ảnh placeholder nếu R.drawable.sample_pc chưa có
    // Ví dụ: painterResource(android.R.drawable.ic_menu_gallery)
    CartScreen()
}
