package com.gtg.electroshopandroid.ui.screen.product

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.gtg.electroshopandroid.data.model.ProductDto
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.unit.Dp
import com.gtg.electroshopandroid.data.model.RatingDto
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
fun ProductScreen(productId: Int) {
    val viewModel: ProductViewModel = viewModel(factory = ProductViewModel.Factory)
    val productUiState = viewModel.productUiState

    LaunchedEffect(productId) {
        viewModel.getProductById(productId)
    }

    when (productUiState) {
        is ProductUiState.Loading -> Text("Đang tải sản phẩm...")
        is ProductUiState.Error -> Text("Không thể tải sản phẩm.")
        is ProductUiState.Success -> {
            val product = (productUiState as ProductUiState.Success).product
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                ProductImageCarousel(product = product)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if (product.discountedPrice < product.originalPrice) {
                    // Nếu có giảm giá → hiển thị cả giá gốc và giá giảm
                    Text(
                        text = "${formatPrice(product.discountedPrice)}₫",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Red
                    )
                    Text(
                        text = "${formatPrice(product.originalPrice)}₫",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough
                    )
                } else {
                    // Không giảm giá → chỉ hiển thị 1 giá
                    Text(
                        text = "${formatPrice(product.originalPrice)}₫",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                ProductTabs(product = product)
//                RatingSection(
//                    productId = productId,
//                    averageRating = product.averageRating ?: 0.0
//                )
            }
        }
    }
}

@Composable
fun ProductImageCarousel(product: ProductDto) {
    val imageUrls = product.productImages.map { it.imageUrl.toAndroidAccessibleUrl() }
    val pagerState = rememberPagerState(initialPage = 0) {
        imageUrls.size
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(8.dp)
        ) {
            if (imageUrls.isNotEmpty()) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                ) { page ->
                    AsyncImage(
                        model = imageUrls[page],
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                // Hiển thị nhãn giảm giá nếu có
                if (product.discountValue > 0) {
                    val isPercentage = product.discountType.equals("Percentage", ignoreCase = true)
                    val discountText = if (isPercentage) "-${product.discountValue.toInt()}%" else "-${product.discountValue.toInt()}₫"
                    val backgroundColor = if (isPercentage) Color.Yellow else Color.Red
                    val textColor = if (isPercentage) Color.Red else Color.White

                    Text(
                        text = discountText,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(backgroundColor, RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Không có ảnh", color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (imageUrls.isNotEmpty()) {
            PagerIndicator(
                pageCount = imageUrls.size,
                currentPage = pagerState.currentPage,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun PagerIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = Color.Blue,
    inactiveColor: Color = Color.DarkGray,
    indicatorWidth: Dp = 8.dp,
    indicatorHeight: Dp = 8.dp,
    spacing: Dp = 8.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(width = indicatorWidth, height = indicatorHeight)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(if (index == currentPage) activeColor else inactiveColor)
            )

            if (index < pageCount - 1) {
                Spacer(modifier = Modifier.width(spacing))
            }
        }
    }
}
@Composable
fun ProductTabs(product: ProductDto) {
    var selectedTab by remember { mutableStateOf(0) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val tabs = listOf("Mô tả sản phẩm", "Chính sách bán hàng")

        tabs.forEachIndexed { index, title ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (selectedTab == index) Color(0xFF4CAF50) else Color(0xFFE0E0E0)) // Xanh lá vs xám nhạt
                    .clickable { selectedTab = index }
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            ) {
                Text(
                    text = title,
                    color = if (selectedTab == index) Color.White else Color.Black
                )
            }
        }
    }


    if (selectedTab == 0) {
        Text(
            text = product.info,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            color = Color.DarkGray,
        )
    } else {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(Color.White)
                .border(1.dp, Color.Black)
                .padding(8.dp)
        ) {
            Column {
                Text("• Miễn phí giao hàng toàn quốc")
                Text("• Đổi trả trong 7 ngày")
                Text("• Bảo hành chính hãng 12 tháng")
                Text("• Tổng đài hỗ trợ 24/7")
            }
        }
    }
}


