package com.gtg.electroshopandroid.ui.screen.cart
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gtg.electroshopandroid.R // Thay thế bằng package của bạn, ví dụ: com.yourcompany.yourapp.R


@Composable
fun CartScreen() {
    // State cho số lượng sản phẩm
    var quantity by remember { mutableStateOf(1) }
    val unitPrice = 36000 // Giá sản phẩm
    val totalPrice = unitPrice * quantity // Tổng tiền tính toán dựa trên số lượng

    var isChecked by remember { mutableStateOf(true) }

    // State cho lựa chọn RAM
    var selectedRam by remember { mutableStateOf("16GB") }
    // State cho lựa chọn SSD
    var selectedSsd by remember { mutableStateOf("256GB") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0E0E0)) // Màu nền xám nhạt giống ảnh
    ) {
        // Top Bar (Header)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Nút Back (ArrowBack)
            IconButton(
                onClick = { /* Xử lý khi nhấn nút back */ },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
            }

            // Tiêu đề "Giỏ hàng"
            Text(
                text = "Giỏ hàng",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .weight(1f) // Chiếm phần lớn không gian
                    .wrapContentWidth(Alignment.CenterHorizontally) // Căn giữa
            )
            // Khoảng trống để căn chỉnh ArrowBack và tiêu đề
            Spacer(modifier = Modifier.width(48.dp)) // Khoảng trống tương tự kích thước của IconButton
        }

        // Nội dung chính của giỏ hàng
        Column(
            modifier = Modifier
                .weight(1f) // Chiếm phần còn lại của màn hình
                .padding(horizontal = 16.dp) // Padding hai bên cho nội dung
        ) {
            // Card sản phẩm
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White) // Màu nền trắng cho card
            ) {
                Row(modifier = Modifier.padding(12.dp)) {
                    // Checkbox ở góc trên bên phải
                    Box(
                        modifier = Modifier
                            .align(Alignment.Top)
                            .offset(x = 8.dp, y = (-8).dp) // Dịch chuyển lên và sang phải một chút
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { isChecked = it },
                            modifier = Modifier
                                .size(24.dp) // Kích thước checkbox
                                .clip(CircleShape) // Bo tròn
                                .border(1.dp, Color.LightGray, CircleShape) // Viền xám
                                .background(Color.White), // Nền trắng
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color.Red, // Màu khi chọn là đỏ
                                uncheckedColor = Color.White,
                                checkmarkColor = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp)) // Khoảng trống để tránh chồng lấn với checkbox

                    // Ảnh sản phẩm
                    Image(
                        painter = painterResource(R.drawable.logo), // Thay thế bằng R.drawable.sample_pc của bạn
                        contentDescription = "Ảnh sản phẩm PC FASTER GAMING",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "PC FASTER GAMING\n10400F - RTX 3050",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 2 // Giới hạn 2 dòng cho tên sản phẩm
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = "Đánh giá", tint = Color.Yellow, modifier = Modifier.size(16.dp))
                            Text(text = "4.8", fontSize = 14.sp)
                        }
                        Text(text = "36,000 đ", fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(4.dp))

                        // Lựa chọn RAM
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("RAM:")
                            Spacer(Modifier.width(4.dp))
                            // OutlinedButton cho 16GB
                            OutlinedButton(
                                onClick = { selectedRam = "16GB" },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (selectedRam == "16GB") Color(0xFFFFF0F0) else Color.Transparent // Nền nhạt khi chọn
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    if (selectedRam == "16GB") Color.Red else Color.LightGray // Viền đỏ khi chọn
                                ),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp), // Padding nhỏ hơn
                                modifier = Modifier.height(32.dp) // Chiều cao cố định
                            ) {
                                Text("16GB", color = if (selectedRam == "16GB") Color.Red else Color.Black)
                            }
                            Spacer(Modifier.width(4.dp))
                            // OutlinedButton cho 32GB
                            OutlinedButton(
                                onClick = { selectedRam = "32GB" },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (selectedRam == "32GB") Color(0xFFFFF0F0) else Color.Transparent
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    if (selectedRam == "32GB") Color.Red else Color.LightGray
                                ),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("32GB", color = if (selectedRam == "32GB") Color.Red else Color.Black)
                            }
                        }

                        // Lựa chọn SSD
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("SSD:")
                            Spacer(Modifier.width(4.dp))
                            // OutlinedButton cho 256GB
                            OutlinedButton(
                                onClick = { selectedSsd = "256GB" },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (selectedSsd == "256GB") Color(0xFFFFF0F0) else Color.Transparent
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    if (selectedSsd == "256GB") Color.Red else Color.LightGray
                                ),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("256GB", color = if (selectedSsd == "256GB") Color.Red else Color.Black)
                            }
                            Spacer(Modifier.width(4.dp))
                            // OutlinedButton cho 500GB
                            OutlinedButton(
                                onClick = { selectedSsd = "500GB" },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (selectedSsd == "500GB") Color(0xFFFFF0F0) else Color.Transparent
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    if (selectedSsd == "500GB") Color.Red else Color.LightGray
                                ),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("500GB", color = if (selectedSsd == "500GB") Color.Red else Color.Black)
                            }
                        }

                        // Bộ điều chỉnh số lượng
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 8.dp) // Thêm padding trên
                        ) {
                            // Nút giảm số lượng
                            IconButton(
                                onClick = { if (quantity > 1) quantity-- },
                                modifier = Modifier
                                    .size(30.dp) // Kích thước nút
                                    .clip(RoundedCornerShape(4.dp))
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Giảm số lượng", tint = Color.Black)
                            }

                            Text(
                                text = quantity.toString(),
                                modifier = Modifier.width(36.dp), // Tăng chiều rộng để số không bị sát
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            // Nút tăng số lượng
                            IconButton(
                                onClick = { quantity++ },
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Tăng số lượng", tint = Color.Black)
                            }
                        }
                    }
                }
            }
        }

        // Phần tổng tiền và nút thanh toán (ở dưới cùng)
        if (isChecked) {
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
                    onClick = { /* xử lý thanh toán */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("THANH TOÁN", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            // Nếu không check thì có thể để khoảng trống hoặc không hiển thị gì
            Spacer(modifier = Modifier.height(16.dp))
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
