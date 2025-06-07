package com.gtg.electroshopandroid.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gtg.electroshopandroid.R
import com.gtg.electroshopandroid.convertBaseUrl
import com.gtg.electroshopandroid.data.model.product.ProductCardDto
import com.gtg.electroshopandroid.formatCurrency
import com.gtg.electroshopandroid.ui.theme.ElectroShopAndroidTheme
import com.gtg.electroshopandroid.ui.theme.inversePrimaryLight
import com.gtg.electroshopandroid.ui.theme.onSurfaceLight

@Composable
fun ProductCard(
    productCardDto: ProductCardDto,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .width(200.dp)
            .padding(8.dp)
    ) {
        Box {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Product Image
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current).data(convertBaseUrl(productCardDto.images[0]))
                        .crossfade(true).build(),
                    error = painterResource(R.drawable.ic_broken_image),
                    placeholder = painterResource(R.drawable.loading_img),
                    contentDescription = productCardDto.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                )

                // Product Info
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = productCardDto.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(lineHeight = 20.sp),
                        modifier = Modifier.height(40.dp)
                    )


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = formatCurrency(productCardDto.originalPrice),
                            style = TextStyle(
                                textDecoration = TextDecoration.LineThrough,
                                color = MaterialTheme.colorScheme.outline,
                                fontSize = 12.sp
                            ),
                        )

                        // Rating Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = productCardDto.averageRating.toString(),
                                fontSize = 12.sp
                            )
                        }
                    }
                    Text(
                        text = formatCurrency(productCardDto.discountedPrice),
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        style = TextStyle(lineHeight = 20.sp)
                    )

                }
            }

            // Favorite Icon
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(inversePrimaryLight, shape = RoundedCornerShape(bottomStart = 8.dp, topEnd = 16.dp))
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = onSurfaceLight,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewProductCardLightTheme() {
    val productCardDto = ProductCardDto(
        productId = 1,
        name = "Laptop Dell Inspiron 15 3520 i5",
        images = listOf(
            "https://cdn2.cellphones.com.vn/insecure/rs:fill:0:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/l/a/laptop-dell-inspiron-15-3520-w15kt_5__1.png",
        ),
        originalPrice = 10000000.0,
        discountedPrice = 9000000.0,
        discountType = "Percentage",
        discountValue = 10.0,
        averageRating = 1.0
    )

    ElectroShopAndroidTheme(darkTheme = false) {
        ProductCard(
            productCardDto = productCardDto,
            isFavorite = false,
            onFavoriteClick = { /* TODO: handle click */ }
        )
    }
}

@Preview
@Composable
fun PreviewProductCardDarkTheme() {
    val productCardDto = ProductCardDto(
        productId = 1,
        name = "Laptop Dell",
        images = listOf(
            "https://cdn2.cellphones.com.vn/insecure/rs:fill:0:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/l/a/laptop-dell-inspiron-15-3520-w15kt_5__1.png",
        ),
        originalPrice = 10000000.0,
        discountedPrice = 8000000.0,
        discountType = "Percentage",
        discountValue = 10.0,
        averageRating = 1.0
    )

    ElectroShopAndroidTheme(darkTheme = true) {
        ProductCard(
            productCardDto = productCardDto,
            isFavorite = false,
            onFavoriteClick = { /* TODO: handle click */ }
        )
    }
}



