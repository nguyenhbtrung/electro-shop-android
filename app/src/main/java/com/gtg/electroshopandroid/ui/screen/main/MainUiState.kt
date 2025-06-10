package com.gtg.electroshopandroid.ui.screen.main

import com.gtg.electroshopandroid.data.model.brand.BrandDto
import com.gtg.electroshopandroid.data.model.category.CategoryTreeDto

data class MainUiState(
    val selectedCategoryParentId: Int = 0,
    val selectedCategoryChildId: Int = 0,
    val categories: List<CategoryTreeDto> = emptyList(),
    val selectedBrandId: Int = 0,
    val brands: List<BrandDto> = emptyList()
)
