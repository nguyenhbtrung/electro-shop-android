package com.gtg.electroshopandroid

import android.app.Application
import com.gtg.electroshopandroid.data.AppContainer
import com.gtg.electroshopandroid.data.DefaultAppContainer


class ElectroShopApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}