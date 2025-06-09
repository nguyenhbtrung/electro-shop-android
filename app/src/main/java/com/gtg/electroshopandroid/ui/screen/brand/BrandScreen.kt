import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.gtg.electroshopandroid.convertBaseUrl
import com.gtg.electroshopandroid.data.model.brand.BrandProductDto
import com.gtg.electroshopandroid.data.model.product.ProductCardDto
import com.gtg.electroshopandroid.ui.components.ProductCard
import com.gtg.electroshopandroid.ui.screen.brand.BrandUiState
import com.gtg.electroshopandroid.ui.screen.brand.BrandViewModel
import com.gtg.electroshopandroid.ui.screen.brand.ProductByBrandUiState
import java.util.Locale
import com.gtg.electroshopandroid.ui.screen.category.ComboBox


@Composable
fun BrandScreen(
    brandId: Int,
    brandName: String,
    onProductClick: (Int) -> Unit,
    onBack: () -> Unit = {},
    viewModel: BrandViewModel = viewModel(factory = BrandViewModel.Factory),
) {
    // Load data khi brandId thay đổi
    LaunchedEffect(brandId) {
        viewModel.getBrands()
        viewModel.getProductByBrandId(brandId)
    }

    val brandUiState = viewModel.brandUiState
    val uiState = viewModel.productByBrandUiState

    var selectedPrice by remember { mutableStateOf("Tất cả") }
    val priceMap = mapOf(
        "Tất cả" to null,
        "Dưới 5tr" to 0,
        "5tr - 10tr" to 1,
        "10tr - 15tr" to 2,
        "15tr - 20tr" to 3,
        "Trên 20tr" to 4
    )

    var selectedCategory by remember { mutableStateOf("Tất cả") }
    val categoryMap = mapOf(
        "Tất cả" to null,
        "Điện thoại" to 1,
        "Laptop" to 2,
        "Phụ kiện" to 3
    )

    var selectedRating by remember { mutableStateOf("") }
    val ratingMap = mapOf(
        "0-1" to 0,
        "1-2" to 1,
        "2-3" to 2,
        "3-4" to 3,
        "4-5" to 4
    )

    var page by remember { mutableStateOf(0) }
    val pageSize = 14

    when (uiState) {
        is ProductByBrandUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ProductByBrandUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = viewModel.errorMessage ?: "Đã có lỗi xảy ra")
            }
        }

        is ProductByBrandUiState.Success -> {
            val products = uiState.products
            val pagedProducts = products.drop(page * pageSize).take(pageSize)
            val matchedBrand = (brandUiState as? BrandUiState.Success)?.brands?.find { it.brandId == brandId }

            Column(modifier = Modifier.fillMaxSize()) {
                // Header (Back button + Title)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Thương Hiệu: $brandName",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                // Nội dung chính scroll được
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Thông tin thương hiệu
                    matchedBrand?.let { brand ->
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                AsyncImage(
                                    model = brand.imageUrl,
                                    contentDescription = brand.brandName,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Đất nước: ${brand.country}",
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Thông tin nhãn hàng:",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = brand.info,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    // Phần filter
                    item {
                        FilterSectionBrand(
                            selectedPrice = selectedPrice,
                            onPriceSelected = {
                                selectedPrice = it
                                page = 0
                            },
                            selectedCategory = selectedCategory,
                            onCategorySelected = {
                                selectedCategory = it
                                page = 0
                            },
                            selectedRating = selectedRating,
                            onRatingSelected = {
                                selectedRating = if (selectedRating == it) "" else it
                                page = 0
                            },
                            onApplyFilter = {
                                page = 0
                                viewModel.getFilteredProducts(
                                    brandId = brandId,
                                    price = priceMap[selectedPrice],
                                    categoryId = categoryMap[selectedCategory],
                                    rating = ratingMap[selectedRating]
                                )
                            }
                        )
                    }

                    // Danh sách sản phẩm dạng lưới 2 cột
                    item {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .heightIn(min = 400.dp, max = 600.dp),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(pagedProducts) { product ->
                                ProductCard(
                                    productCardDto = product.toProductCardDto(),
                                    onFavoriteClick = { /* TODO */ },
                                    onProductClick = { onProductClick(product.productId) }
                                )
                            }
                        }
                    }

                    // Có thể thêm paging control (next/prev) ở đây nếu cần
                }
            }
        }
    }
}

fun BrandProductDto.toProductCardDto(): ProductCardDto {
    return ProductCardDto(
        productId = this.productId,
        name = this.name,
        originalPrice = this.originalPrice,
        discountedPrice = this.discountedPrice,
        averageRating = String.format(Locale.US, "%.1f", this.averageRating ?: 0.0).toDouble(),
        images = this.images.map { convertBaseUrl(it) },
        discountType = this.discountType,
        discountValue = this.discountValue,
        isFavorite = this.isFavorite
    )
}

@Composable
fun FilterSectionBrand(
    selectedPrice: String,
    onPriceSelected: (String) -> Unit,
    selectedRating: String,
    onRatingSelected: (String) -> Unit,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onApplyFilter: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFFF9F9F9))
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
                Spacer(Modifier.width(8.dp))
                Text("Bộ lọc", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Toggle",
                    tint = Color.Gray
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text("Giá tiền:", modifier = Modifier.weight(1f))
                        ComboBox(
                            options = listOf("Tất cả", "Dưới 5tr", "5tr - 10tr", "10tr - 15tr", "15tr - 20tr", "Trên 20tr"),
                            selectedOption = selectedPrice,
                            onOptionSelected = onPriceSelected
                        )
                    }

                    Divider()

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Đánh giá:", modifier = Modifier.padding(bottom = 4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            listOf("0-1", "1-2", "2-3", "3-4", "4-5").forEach { option ->
                                val isSelected = selectedRating == option
                                OutlinedButton(
                                    onClick = {
                                        if (isSelected) {
                                            onRatingSelected("")  // Bỏ chọn nếu click lại
                                        } else {
                                            onRatingSelected(option)
                                        }
                                    },
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

                            if (selectedRating == "4-5") {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Best Rating",
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(start = 4.dp)
                                )
                            }
                        }
                    }

                    Divider()

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text("Danh mục:", modifier = Modifier.weight(1f))
                        ComboBox(
                            options = listOf("Tất cả", "Điện thoại", "Laptop", "Phụ kiện"),
                            selectedOption = selectedCategory,
                            onOptionSelected = onCategorySelected
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
        }
    }
}

