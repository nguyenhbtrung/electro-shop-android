package com.gtg.electroshopandroid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gtg.electroshopandroid.R
import com.gtg.electroshopandroid.data.model.brand.BrandDto
import com.gtg.electroshopandroid.data.model.category.CategoryTreeDto
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreBottomSheet() {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        ExploreSheet(
            sheetState = sheetState,
            onDismiss = {
                scope.launch {
                    sheetState.hide()
                    showSheet = false
                }
            }
        )
    }

    // Nút để mở Bottom Sheet
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { showSheet = true }) {
            Text("Hiện Khám Phá")
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        // ✅ Bạn có thể thay nội dung này theo thiết kế của bạn
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Khám phá", style = MaterialTheme.typography.titleLarge)
                    IconButton(onClick = { /* đóng sheet */ }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }
    }
}

val categories = listOf(
    CategoryTreeDto(
        categoryId = 7,
        name = "PC",
        childs = listOf(
            CategoryTreeDto(categoryId = 9, name = "PC Văn phòng", childs = emptyList()),
            CategoryTreeDto(categoryId = 10, name = "PC Gaming", childs = emptyList())
        )
    ),
    CategoryTreeDto(
        categoryId = 8,
        name = "Laptop",
        childs = listOf(
            CategoryTreeDto(categoryId = 11, name = "Laptop Dell", childs = emptyList()),
            CategoryTreeDto(categoryId = 12, name = "Laptop Asus", childs = emptyList()),
            CategoryTreeDto(categoryId = 13, name = "Laptop lenovo", childs = emptyList())
        )
    ),
    CategoryTreeDto(
        categoryId = 8,
        name = "Laptop",
        childs = listOf(
            CategoryTreeDto(categoryId = 11, name = "Laptop Dell", childs = emptyList()),
            CategoryTreeDto(categoryId = 12, name = "Laptop Asus", childs = emptyList()),
            CategoryTreeDto(categoryId = 13, name = "Laptop lenovo", childs = emptyList())
        )
    ),
    CategoryTreeDto(
        categoryId = 8,
        name = "Laptop",
        childs = listOf(
            CategoryTreeDto(categoryId = 11, name = "Laptop Dell", childs = emptyList()),
            CategoryTreeDto(categoryId = 12, name = "Laptop Asus", childs = emptyList()),
            CategoryTreeDto(categoryId = 13, name = "Laptop lenovo", childs = emptyList())
        )
    ),
    CategoryTreeDto(
        categoryId = 8,
        name = "Laptop",
        childs = listOf(
            CategoryTreeDto(categoryId = 11, name = "Laptop Dell", childs = emptyList()),
            CategoryTreeDto(categoryId = 12, name = "Laptop Asus", childs = emptyList()),
            CategoryTreeDto(categoryId = 13, name = "Laptop lenovo", childs = emptyList())
        )
    ),
    CategoryTreeDto(
        categoryId = 8,
        name = "Laptop",
        childs = listOf(
            CategoryTreeDto(categoryId = 11, name = "Laptop Dell", childs = emptyList()),
            CategoryTreeDto(categoryId = 12, name = "Laptop Asus", childs = emptyList()),
            CategoryTreeDto(categoryId = 13, name = "Laptop lenovo", childs = emptyList())
        )
    )
)

val fakeBrands = listOf(
    BrandDto(
        brandId = 1,
        brandName = "Apple",
        country = "USA",
        imageUrl = "https://example.com/apple.png",
        info = "Premium electronics and lifestyle products."
    ),
    BrandDto(
        brandId = 2,
        brandName = "Samsung",
        country = "South Korea",
        imageUrl = "https://example.com/samsung.png",
        info = "Global leader in technology and innovation."
    ),
    BrandDto(
        brandId = 3,
        brandName = "Sony",
        country = "Japan",
        imageUrl = "https://example.com/sony.png",
        info = "Entertainment and high-quality electronics."
    ),
    BrandDto(
        brandId = 4,
        brandName = "Xiaomi",
        country = "China",
        imageUrl = "https://example.com/xiaomi.png",
        info = "Affordable and smart tech gadgets."
    ),
    BrandDto(
        brandId = 5,
        brandName = "LG",
        country = "South Korea",
        imageUrl = "https://example.com/lg.png",
        info = "Home appliances and smart electronics."
    )
)

@Composable
fun ExploreSheetContent(
    onDismiss: () -> Unit,
    onParentCategoryClick: (Int) -> Unit,
    onChildCategoryClick: (Int, String) -> Unit,
    onBrandClick: (Int, String) -> Unit,
    categories: List<CategoryTreeDto>,
    selectedCategoryParentId: Int,
    selectedCategoryChildId: Int,
    brands: List<BrandDto>,
    selectedBrandId: Int
) {
    val selectedCategoryParent = categories.find { it.categoryId == selectedCategoryParentId }
    val categoryChilds = selectedCategoryParent?.childs ?: emptyList()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .padding(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.explore),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = stringResource(R.string.close))
            }

        }
        Spacer(Modifier.height(28.dp))
        Text(
            text = stringResource(R.string.categories),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            items(categories) { cat ->
                OutlinedButton(
                    onClick = { onParentCategoryClick(cat.categoryId) },
                    colors = if (selectedCategoryParentId == cat.categoryId) ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    ) else ButtonDefaults.outlinedButtonColors(),
                ) {
                    Text(
                        text = cat.name,
                        color = if (selectedCategoryParentId == cat.categoryId)
                            MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        HorizontalDivider(
            thickness = 2.dp,
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categoryChilds.forEach { cat ->
                OutlinedButton(
                    onClick = { onChildCategoryClick(cat.categoryId, cat.name) },
                    colors = if (selectedCategoryChildId == cat.categoryId)
                        ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                        )
                    else ButtonDefaults.outlinedButtonColors()
                ) {
                    Text(
                        text = cat.name,
                        color = if (selectedCategoryChildId == cat.categoryId)
                            MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        Spacer(Modifier.height(28.dp))
        Text(
            text = stringResource(R.string.brand),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            items(brands) { brand ->
                val isSelected = brand.brandId == selectedBrandId

                val backgroundColor = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }

                val textColor = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }

                Box(
                    modifier = Modifier
                        .clickable { onBrandClick(brand.brandId, brand.brandName) }
                        .background(
                            color = backgroundColor,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = brand.brandName,
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}



@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewExploreBottomSheet() {
    MaterialTheme {
        ExploreBottomSheet()
    }
}

