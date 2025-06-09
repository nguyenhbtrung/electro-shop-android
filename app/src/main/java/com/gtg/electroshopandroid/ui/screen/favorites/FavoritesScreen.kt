package com.gtg.electroshopandroid.ui.screen.favorites


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.gtg.electroshopandroid.R
import com.gtg.electroshopandroid.ui.theme.ElectroShopAndroidTheme
import com.gtg.electroshopandroid.convertBaseUrl
import com.gtg.electroshopandroid.formatCurrency


@Composable
fun FavoritesScreen(
    navController: NavHostController
) {
    val viewModel: FavoriteViewModel = viewModel(factory = FavoriteViewModel.Factory)
    val favorites by viewModel.favorites.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(true) {
        viewModel.loadFavorites()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Chiều cao giống TopAppBar chuẩn
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Danh Sách Yêu Thích",
                style = MaterialTheme.typography.titleLarge,
                // hoặc titleLarge tùy bạn
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Đang tải...", style = MaterialTheme.typography.bodyMedium)
            }
        } else if (favorites.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Không có sản phẩm yêu thích", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(favorites) { product ->
                    ProductCard(
                        productName = product.name,
                        originalPrice = product.originalPrice,
                        discountedPrice = product.discountedPrice,
                        imageUrl = convertBaseUrl(product.imageUrl),
                        onRemoveClick = {
                            viewModel.toggleFavorite(product.productId)
                        },
                        onClick = {
                            navController.navigate("products/${product.productId}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    productName: String,
    originalPrice: Double,
    discountedPrice: Double,
    imageUrl: String,
    onRemoveClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Product Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = productName,
                    style = MaterialTheme.typography.titleMedium
                )

                if (originalPrice > discountedPrice) {
                    Row {
                        Text(
                            text = formatCurrency(originalPrice),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.Gray,
                                textDecoration = TextDecoration.LineThrough
                            ),
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(
                            text = formatCurrency(discountedPrice),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.Red
                            )
                        )
                    }
                } else {
                    Text(
                        text = formatCurrency(discountedPrice),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onRemoveClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Bỏ yêu thích", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProductCard() {
    ElectroShopAndroidTheme {
        ProductCard(
            productName = "ASUS ROG Strix G15",
            originalPrice = 40000000.0,
            discountedPrice = 35000000.0,
            imageUrl = R.drawable.logo.toString(),
            onRemoveClick = {},
            onClick = {}
        )
    }
}