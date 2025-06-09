package com.gtg.electroshopandroid.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gtg.electroshopandroid.ui.components.ProductCard
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.getValue

@Composable
fun SearchResultsScreen(
    query: String,
    navController: NavController,
    onBack: () -> Unit = {}
) {
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
    val searchUiState by viewModel.searchUiState.collectAsState()

    LaunchedEffect(query) {
        viewModel.searchProductsByName(query)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Nút quay lại ở trên cùng
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            androidx.compose.material3.IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.Black
                )
            }

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp) // tránh bị nút đè
            ) {
                Text(
                    text = "Kết quả tìm kiếm cho: \"$query\"",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp)
                )

                when (searchUiState) {
                    is SearchUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                    is SearchUiState.Success -> {
                        val products = (searchUiState as SearchUiState.Success).products
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            items(products) { productCardDto ->
                                ProductCard(
                                    productCardDto = productCardDto,
                                    onFavoriteClick = { /* TODO */ },
                                    onProductClick = {
                                        navController.navigate("products/${productCardDto.productId}")
                                    }
                                )
                            }
                        }
                    }

                    is SearchUiState.Error -> {
                        Text(
                            text = "Đã xảy ra lỗi khi tìm kiếm.",
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

