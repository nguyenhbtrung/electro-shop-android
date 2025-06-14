package com.gtg.electroshopandroid.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.gtg.electroshopandroid.data.model.product.ProductCardDto

@Composable
fun HorizontalScrollingProductList(
    productCardDtoList: List<ProductCardDto>,
    title: String,
    onFavoriteClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(productCardDtoList) { dto ->
                ProductCard(
                    productCardDto = dto,
                    onFavoriteClick = onFavoriteClick,
                    onProductClick = { navController.navigate("products/${dto.productId}")}
                )
            }
        }
    }
}