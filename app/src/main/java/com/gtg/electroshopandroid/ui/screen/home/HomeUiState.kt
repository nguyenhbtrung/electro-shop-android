package com.gtg.electroshopandroid.ui.screen.home

import com.gtg.electroshopandroid.data.model.BannerDto


data class HomeUiState(
    val banners: List<BannerDto> = initLoadingBanners()
)

sealed interface BannerUiState {
    data class Success(val banners: List<BannerDto>) : BannerUiState
    data object Error : BannerUiState
    data object Loading : BannerUiState
}

fun initLoadingBanners() : List<BannerDto> {
    return listOf(
        BannerDto(1, "", "", ""),
        BannerDto(2, "", "", ""),
        BannerDto(3, "", "", "")
    )
}
