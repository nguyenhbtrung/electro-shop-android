package com.gtg.electroshopandroid.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.gtg.electroshopandroid.R
import kotlinx.coroutines.delay

// Model cho dữ liệu banner
data class BannerItem(
    val imageUrl: String,
    val contentDescription: String? = null
)

@Composable
fun BannerCarousel(
    banners: List<BannerItem>,
    modifier: Modifier = Modifier,
    autoScrollDuration: Long = 3000L, // Thời gian tự động cuộn (ms)
    indicatorColor: Color = Color.Gray,
    selectedIndicatorColor: Color = Color.Red,
    cardElevation: Dp = 4.dp
) {
    if (banners.isEmpty()) {
        Spacer(modifier = modifier.height(200.dp)) // Hoặc hiển thị placeholder khác
        return
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { banners.size }
    )

    // Tự động cuộn
    LaunchedEffect(pagerState) {
        if (autoScrollDuration > 0) {
            while (true) {
                delay(autoScrollDuration)
                val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp), // Tùy chỉnh chiều cao của carousel
            elevation = CardDefaults.cardElevation(defaultElevation = cardElevation)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val banner = banners[page]
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current).data(banner.imageUrl)
                        .crossfade(true).build(),
                    error = painterResource(R.drawable.banner_1),
                    placeholder = painterResource(R.drawable.banner_1),
                    contentDescription = banner.contentDescription,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Indicator dots
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            banners.indices.forEach { index ->
                val color = if (pagerState.currentPage == index) selectedIndicatorColor else indicatorColor
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                        .padding(horizontal = 4.dp)
                )
                if (index < banners.size - 1) {
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }
}

// Preview cho BannerCarousel
@Composable
@Preview(showBackground = true)
fun BannerCarouselPreview() {
    val sampleBanners = listOf(
        BannerItem("https://picsum.photos/id/237/200/300", "Banner 1"),
        BannerItem("https://picsum.photos/id/238/200/300", "Banner 2"),
        BannerItem("https://picsum.photos/id/239/200/300", "Banner 3")
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(16.dp))
        BannerCarousel(
            banners = sampleBanners,
            modifier = Modifier.padding(horizontal = 16.dp),
            autoScrollDuration = 3000L
        )
        Spacer(modifier = Modifier.height(16.dp))
        BannerCarousel(
            banners = sampleBanners.take(1), // Chỉ 1 banner, không có indicator
            modifier = Modifier.padding(horizontal = 16.dp),
            autoScrollDuration = 0L // Tắt tự động cuộn
        )
    }
}