package com.gtg.electroshopandroid

import android.app.Application
import com.gtg.electroshopandroid.data.AppContainer
import com.gtg.electroshopandroid.data.DefaultAppContainer
import com.stripe.android.PaymentConfiguration


class ElectroShopApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51RWwDvQjO3Y8yCfk6wwNhjVBXAinhs060mONdn8gXtWjhokvWUNLVOXVEHvOlThLvgxKgZ1OPXJN0QZ7C4wUqpQZ005wXZVEmD"
        )
    }
}