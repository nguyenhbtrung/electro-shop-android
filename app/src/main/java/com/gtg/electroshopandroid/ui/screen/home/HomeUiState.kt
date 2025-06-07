package com.gtg.electroshopandroid.ui.screen.home

import com.gtg.electroshopandroid.data.model.BannerDto
import com.gtg.electroshopandroid.data.model.product.ProductCardDto


data class HomeUiState(
    val banners: List<BannerDto> = initLoadingBanners(),
    val discountedProducts: List<ProductCardDto> = initLoadingProducts(),
    val bestSellerProducts: List<ProductCardDto> = initLoadingProducts(),
)

sealed interface BannerUiState {
    data class Success(val banners: List<BannerDto>) : BannerUiState
    data object Error : BannerUiState
    data object Loading : BannerUiState
}

fun initLoadingBanners() : List<BannerDto> {
    return listOf(
        BannerDto(1, "", ""),
        BannerDto(2, "", ""),
        BannerDto(3, "", "")
    )
}

fun initLoadingProducts() : List<ProductCardDto> {
    return List(10){ index ->
        ProductCardDto(index + 1, "", listOf(""), 0.0, 0.0, "", 0.0, 0.0)
    }
}
