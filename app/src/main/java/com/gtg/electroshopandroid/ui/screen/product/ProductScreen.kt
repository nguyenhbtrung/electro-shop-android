package com.gtg.electroshopandroid.ui.screen.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.gtg.electroshopandroid.data.model.ProductDto
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import com.google.accompanist.pager.HorizontalPagerIndicator

fun String.toAndroidAccessibleUrl(): String {
    return this.replace("https://localhost", "http://10.0.2.2")
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
                Text(
                    text = "Giá: ${product.discountedPrice}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color(0xFF388E3C) // màu xanh lá ví dụ
                )
                // Bạn có thể thêm các thông tin chi tiết khác ở đây
            }
        }
    }
}

@Composable
fun ProductImageCarousel(product: ProductDto) {
    val pagerState = rememberPagerState()
    val imageUrls = product.productImages.map { it.imageUrl.toAndroidAccessibleUrl() }

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
                    count = imageUrls.size,
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
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
            HorizontalPagerIndicator(
                pagerState = pagerState,
                activeColor = Color.Blue,
                inactiveColor = Color.DarkGray,
                indicatorWidth = 8.dp,
                indicatorHeight = 8.dp,
                spacing = 8.dp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
