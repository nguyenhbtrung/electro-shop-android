import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gtg.electroshopandroid.convertBaseUrl
import com.gtg.electroshopandroid.data.model.ProductDto
import com.gtg.electroshopandroid.data.model.category.CategoryProductDto
import com.gtg.electroshopandroid.data.model.product.ProductCardDto
import com.gtg.electroshopandroid.ui.components.ProductCard
import com.gtg.electroshopandroid.ui.screen.category.CategoryViewModel
import com.gtg.electroshopandroid.ui.screen.category.ProductByCategoryUiState

@Composable
fun CategoryScreen(
    categoryId: Int,
    viewModel: CategoryViewModel = viewModel(factory = CategoryViewModel.Factory),
) {
    LaunchedEffect(categoryId) {
        viewModel.getProductByCategoryId(categoryId)
    }

    val uiState = viewModel.productByCategoryUiState

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

            // Tính sản phẩm của trang hiện tại
            val pagedProducts = products.drop(page * pageSize).take(pageSize)

            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "DANH MỤC SẢN PHẨM",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
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
