package com.gtg.electroshopandroid.ui.screen.product

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel


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
            Column {
                Text(product.name)
                Text("Giá: ${product.discountedPrice}")
                // Hiển thị ảnh và thông tin khác
            }
        }
    }
}
