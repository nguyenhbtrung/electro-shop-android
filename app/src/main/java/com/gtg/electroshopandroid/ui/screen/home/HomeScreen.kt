package com.gtg.electroshopandroid.ui.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.gtg.electroshopandroid.R
import com.gtg.electroshopandroid.data.model.BannerDto
import com.gtg.electroshopandroid.data.model.product.ProductCardDto
import com.gtg.electroshopandroid.ui.components.BannerCarousel
import com.gtg.electroshopandroid.ui.components.BannerItem
import com.gtg.electroshopandroid.ui.components.HorizontalScrollingProductList
import com.gtg.electroshopandroid.ui.theme.ElectroShopAndroidTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
) {

    val productList = List(10) { index ->
        ProductCardDto(
            productId = index + 1,
            name = "Sản phẩm $index",
            images = listOf(
                "https://cdn2.cellphones.com.vn/insecure/rs:fill:0:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/l/a/laptop-dell-inspiron-15-3520-w15kt_5__1.png"
            ),
            originalPrice = 10000000.0,
            discountedPrice = 9000000.0,
            discountType = "Percentage",
            discountValue = 10.0,
            averageRating = 4.0
        )
    }
    val scrollState = rememberScrollState()

    val homeViewModel: HomeViewModel =
        viewModel(factory = HomeViewModel.Factory)

    val searchText = homeViewModel.searchText

    Box(modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = homeViewModel::onSearchTextChanged,
                label = { Text(stringResource(R.string.home_search_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                singleLine = true,
                trailingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                },
                shape = RoundedCornerShape(32.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )
            Spacer(Modifier.height(32.dp))
            BannerCarousel(
                banners = homeViewModel.uiState.banners,
                modifier = Modifier.padding(horizontal = 16.dp),
                autoScrollDuration = 3000L,
                indicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedIndicatorColor = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(16.dp))
            HorizontalScrollingProductList(
                title = stringResource(R.string.discount),
                productCardDtoList = homeViewModel.uiState.discountedProducts,
                navController = navController
            )
            Spacer(Modifier.height(16.dp))
            HorizontalScrollingProductList(
                title = stringResource(R.string.best_seller),
                productCardDtoList = homeViewModel.uiState.bestSellerProducts,
                navController = navController
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreviewLightTheme() {
    ElectroShopAndroidTheme {
        HomeScreen()
    }
}

@Preview
@Composable
fun HomeScreenPreviewDarkTheme() {
    ElectroShopAndroidTheme(darkTheme = true) {
        HomeScreen()
    }
}



