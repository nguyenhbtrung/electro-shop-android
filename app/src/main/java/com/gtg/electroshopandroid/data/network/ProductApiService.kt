package com.gtg.electroshopandroid.data.network;

import com.gtg.electroshopandroid.data.model.ProductDto;
import com.gtg.electroshopandroid.data.model.product.ProductCardDto
import retrofit2.http.GET;
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {
    @GET("api/Product/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductDto

    @GET("api/Product/discounted")
    suspend fun getDiscountedProducts(): List<ProductCardDto>

    @GET("api/Product/by_user")
    suspend fun getBestSellerProducts(): List<ProductCardDto>

    @GET("api/Filter/search")
    suspend fun getProductsSearch(@Query("productName")productName:String):List<ProductCardDto>
}