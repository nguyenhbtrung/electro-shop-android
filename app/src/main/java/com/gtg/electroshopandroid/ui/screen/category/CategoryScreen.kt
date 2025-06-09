package com.gtg.electroshopandroid.ui.screen.category
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gtg.electroshopandroid.convertBaseUrl
import com.gtg.electroshopandroid.data.model.category.CategoryProductDto
import com.gtg.electroshopandroid.data.model.product.ProductCardDto
import com.gtg.electroshopandroid.ui.components.ProductCard
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun CategoryScreen(
    categoryId: Int,
    viewModel: CategoryViewModel = viewModel(factory = CategoryViewModel.Factory),
) {
    LaunchedEffect(categoryId) {
        viewModel.getProductByCategoryId(categoryId)
    }

    val uiState = viewModel.productByCategoryUiState
    var selectedPrice by remember { mutableStateOf("Tất cả") }
    val priceMap = mapOf(
        "Tất cả" to null,
        "Dưới 5tr" to 0,
        "5tr - 10tr" to 1,
        "10tr - 15tr" to 2,
        "15tr - 20tr" to 3,
        "Trên 20tr" to 4
    )
    var selectedBrand by remember { mutableStateOf("Tất cả") }
    val brandMap = mapOf(
        "Asus" to 2,
        "Samsung" to 4,
        "Apple" to 1005,
        "Sony" to 1006,
        "Dell" to 1007,
        "Hp" to 1008,
        "Acer" to 1010,
        "Tất cả" to null
    )
    var selectedRating by remember { mutableStateOf("0-5") }
    val ratingMap = mapOf(
        "0-1" to 0,
        "0-2" to 1,
        "2-3" to 2,
        "3-4" to 3,
        "4-5" to 4
    )
    var page by remember { mutableStateOf(0) }
    val pageSize = 14

    when (uiState) {
        is ProductByCategoryUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Đang tải dữ liệu...")
            }
        }
        is ProductByCategoryUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = viewModel.errorMessage ?: "Đã có lỗi xảy ra")
            }
        }
        is ProductByCategoryUiState.Success -> {
            val products = (uiState as ProductByCategoryUiState.Success).products

            val pagedProducts = products.drop(page * pageSize).take(pageSize)
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "DANH MỤC SẢN PHẨM",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )
                FilterSection(
                    selectedPrice = selectedPrice,
                    onPriceSelected = { selectedPrice = it; page = 0 },
                    selectedBrand = selectedBrand,
                    onBrandSelected = { selectedBrand = it; page = 0 },
                    selectedRating = selectedRating,
                    onRatingSelected = { selectedRating = it; page = 0 },
                    onApplyFilter = {
                        page = 0
                        viewModel.getFilteredProducts(
                            categoryId = categoryId,
                            price = priceMap[selectedPrice],
                            brandId = brandMap[selectedBrand],
                            rating = ratingMap[selectedRating]
                        )
                    },
                )


                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(pagedProducts) { product ->
                        ProductCard(
                            productCardDto = product.toProductCardDto(),
                            isFavorite = false,
                            onFavoriteClick = { /* TODO */ },
                            onProductClick = {  }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { if (page > 0) page-- },
                        enabled = page > 0
                    ) {
                        Text("Trang trước")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { if ((page + 1) * pageSize < products.size) page++ },
                        enabled = (page + 1) * pageSize < products.size
                    ) {
                        Text("Trang sau")
                    }
                }
            }
        }
    }
}
fun CategoryProductDto.toProductCardDto(): ProductCardDto  {
    return ProductCardDto(
        productId = this.productId,
        name = this.name,
        originalPrice = this.originalPrice,
        discountedPrice = this.discountedPrice,
        averageRating = this.averageRating,
        images =  this.images.map { convertBaseUrl(it) },
        discountType = this.discountType,
        discountValue = this.discountValue,
    )
}
@Composable
fun FilterSection(
    selectedPrice: String,
    onPriceSelected: (String) -> Unit,
    selectedRating: String,
    onRatingSelected: (String) -> Unit,
    selectedBrand: String,
    onBrandSelected: (String) -> Unit,
    onApplyFilter: () -> Unit
) {

    val priceOptions = listOf(
        "Tất cả", "Dưới 5tr", "5tr - 10tr", "10tr - 15tr", "15tr - 20tr", "Trên 20tr"
    )

    val ratingOptions = listOf("0-1", "1-2", "2-3", "3-4", "4-5")

    val brandList = listOf("Tất cả", "Asus", "Samsung", "Apple", "Sony", "Dell", "Hp", "Acer")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1F1F1))
            .padding(8.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(Icons.Default.Menu, contentDescription = "Menu")
            Spacer(Modifier.width(8.dp))
            Text("Bộ lọc", fontWeight = FontWeight.Bold)
        }

        // Giá tiền
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Giá tiền:", modifier = Modifier.weight(1f))
            ComboBox(
                options = priceOptions,
                selectedOption = selectedPrice,
                onOptionSelected = onPriceSelected
            )
        }

        Divider()

        // Đánh giá
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Đánh giá:", modifier = Modifier.weight(1f))
            ratingOptions.forEach { option ->
                val isSelected = selectedRating == option
                OutlinedButton(
                    onClick = { onRatingSelected(option) },
                    border = if (isSelected) BorderStroke(2.dp, Color(0xFFFFC107)) else null,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isSelected) Color(0xFFFFF8E1) else Color.White
                    ),
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .height(36.dp)
                ) {
                    Text(option)
                }
            }
            // Icon ngôi sao cho mức cao nhất
            if (selectedRating == "4-5") {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Best Rating",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(24.dp).padding(start = 4.dp)
                )
            }
        }

        Divider()

        // Nhãn hàng
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Nhãn hàng:", modifier = Modifier.weight(1f))
            ComboBox(
                options = brandList,
                selectedOption = selectedBrand,
                onOptionSelected = onBrandSelected
            )
        }
        Button(
            onClick = onApplyFilter,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Icon(Icons.Default.FilterList, contentDescription = "Lọc")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Áp dụng bộ lọc")
        }

    }
}

// ComboBox (DropdownMenu) đơn giản
@Composable
fun ComboBox(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(
            onClick = { expanded = true },
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(selectedOption)
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
