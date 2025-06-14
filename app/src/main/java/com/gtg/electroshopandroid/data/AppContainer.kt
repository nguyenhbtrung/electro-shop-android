package com.gtg.electroshopandroid.data

import android.content.Context
import com.gtg.electroshopandroid.data.network.AuthApiService
import com.gtg.electroshopandroid.data.repository.AuthRepository
import com.gtg.electroshopandroid.data.repository.AuthRepositoryImpl
import com.gtg.electroshopandroid.data.interceptor.AuthInterceptor
import com.gtg.electroshopandroid.data.network.BannerApiService
import com.gtg.electroshopandroid.data.network.BrandApiService
import com.gtg.electroshopandroid.data.network.CartApiService
import com.gtg.electroshopandroid.data.network.CategoryApiService
import com.gtg.electroshopandroid.data.network.ExampleApiService
import com.gtg.electroshopandroid.data.network.FavoriteApiService
import com.gtg.electroshopandroid.data.network.ProductApiService
import com.gtg.electroshopandroid.data.network.ProductHistoryApiService
import com.gtg.electroshopandroid.data.repository.ExampleRepository
import com.gtg.electroshopandroid.data.repository.ExampleRepositoryImpl
import com.gtg.electroshopandroid.data.network.OrderHistoryApiService
import com.gtg.electroshopandroid.data.network.ReturnHistoryApiService
import com.gtg.electroshopandroid.data.network.ProfileDetailApiService
import com.gtg.electroshopandroid.data.network.RatingApiService
import com.gtg.electroshopandroid.data.network.RecommendApiService
import com.gtg.electroshopandroid.data.repository.BannerRepository
import com.gtg.electroshopandroid.data.repository.BannerRepositoryImpl
import com.gtg.electroshopandroid.data.repository.BrandRepository
import com.gtg.electroshopandroid.data.repository.CartRepository
import com.gtg.electroshopandroid.data.repository.CartRepositoryImpl
import com.gtg.electroshopandroid.data.repository.CategoryRepository
import com.gtg.electroshopandroid.data.repository.FavoriteRepository
import com.gtg.electroshopandroid.data.repository.FavoriteRepositoryImpl
import com.gtg.electroshopandroid.data.repository.OrderHistoryRepository
import com.gtg.electroshopandroid.data.repository.OrderHistoryRepositoryImpl
import com.gtg.electroshopandroid.data.repository.ReturnHistoryRepository
import com.gtg.electroshopandroid.data.repository.ReturnHistoryRepositoryImpl
import com.gtg.electroshopandroid.data.repository.ProductHistoryRepository
import com.gtg.electroshopandroid.data.repository.ProductHistoryRepositoryImpl
import com.gtg.electroshopandroid.data.repository.ProductRepository
import com.gtg.electroshopandroid.data.repository.ProductRepositoryImpl
import com.gtg.electroshopandroid.data.repository.ProfileRepository
import com.gtg.electroshopandroid.data.repository.ProfileRepositoryImpl
import com.gtg.electroshopandroid.data.repository.RatingRepository
import com.gtg.electroshopandroid.data.repository.RecommendRepository
import com.gtg.electroshopandroid.data.repository.RecommendRepositoryImpl
import com.gtg.electroshopandroid.preferences.TokenPreferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

// Biến môi trường tạm thời sửa lại sau
object EnvVariable {
    const val OLD_HOST_BACKEND = "https://localhost:7169/"
    const val BASE_URL = "http://10.0.2.2:5030/"
}

interface AppContainer {
    val exampleRepository: ExampleRepository
    val productHistoryRepository: ProductHistoryRepository
    val productRepository :ProductRepository
    val orderHistoryRepository: OrderHistoryRepository
    val returnHistoryRepository: ReturnHistoryRepository
    val authRepository: AuthRepository
    val tokenPreferences: TokenPreferences
    val ratingRepository: RatingRepository
    val categoryRepository: CategoryRepository
    val recommendRepository: RecommendRepository
    val cartRepository: CartRepository
    val bannerRepository: BannerRepository
    val profileRepository: ProfileRepository
    val favoriteRepository: FavoriteRepository
    val brandRepository: BrandRepository
}

class DefaultAppContainer(
    private val context: Context
) : AppContainer {
    override val tokenPreferences by lazy {
        TokenPreferences(context)
    }

    private val authInterceptor by lazy {
        AuthInterceptor(tokenPreferences)
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    private val json = Json {
        ignoreUnknownKeys = true
    }
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(EnvVariable.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val exampleApiService: ExampleApiService by lazy {
        retrofit.create(ExampleApiService::class.java)
    }

    override val exampleRepository: ExampleRepository by lazy {
        ExampleRepositoryImpl(exampleApiService)
    }

    private val productHistoryApiService: ProductHistoryApiService by lazy {
        retrofit.create(ProductHistoryApiService::class.java)
    }

    override val productHistoryRepository: ProductHistoryRepository by lazy {
        ProductHistoryRepositoryImpl(productHistoryApiService)
    }
    private val productApiService: ProductApiService by lazy {
        retrofit.create(ProductApiService::class.java)
    }
    override val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl(productApiService)
    }

    private val ratingApiService: RatingApiService by lazy {
        retrofit.create(RatingApiService::class.java)
    }

    override val ratingRepository: RatingRepository by lazy {
        RatingRepository(ratingApiService)
    }
    private val categoryApiService: CategoryApiService by lazy {
        retrofit.create(CategoryApiService::class.java)
    }

    override val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(categoryApiService)
    }


    private val recommendApiService: RecommendApiService by lazy {
            retrofit.create(RecommendApiService::class.java)
    }
    override val recommendRepository: RecommendRepository by lazy {
        RecommendRepositoryImpl(recommendApiService)
    }


    private val orderHistoryApiService: OrderHistoryApiService by lazy {
        retrofit.create(OrderHistoryApiService::class.java)
    }
    override val orderHistoryRepository: OrderHistoryRepository by lazy {
        OrderHistoryRepositoryImpl(orderHistoryApiService)
    }

    private val returnHistoryApiService: ReturnHistoryApiService by lazy {
        retrofit.create(ReturnHistoryApiService::class.java)
    }
    override val returnHistoryRepository: ReturnHistoryRepository by lazy {
        ReturnHistoryRepositoryImpl(returnHistoryApiService)
    }

    private val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(authApiService)
    }

    private val cartApiService: CartApiService by lazy {
        retrofit.create(CartApiService::class.java)
    }

    override val cartRepository: CartRepository by lazy {
        CartRepositoryImpl(cartApiService)
    }

    private val bannerApiService: BannerApiService by lazy {
        retrofit.create(BannerApiService::class.java)
    }

    override val bannerRepository: BannerRepository by lazy {
        BannerRepositoryImpl(bannerApiService)
    }

    private val profileApiService: ProfileDetailApiService by lazy {
        retrofit.create(ProfileDetailApiService::class.java)
    }

    override val profileRepository: ProfileRepository by lazy {
        ProfileRepositoryImpl(profileApiService)
    }

    private val favoriteApiService: FavoriteApiService by lazy {
        retrofit.create(FavoriteApiService::class.java)
    }

    override val favoriteRepository: FavoriteRepository by lazy {
        FavoriteRepositoryImpl(favoriteApiService)
    }

    private val brandApiService: BrandApiService by lazy {
        retrofit.create(BrandApiService::class.java)
    }

    override val brandRepository: BrandRepository by lazy {
        BrandRepository(brandApiService)
    }

}