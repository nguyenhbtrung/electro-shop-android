package com.gtg.electroshopandroid.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.gtg.electroshopandroid.navigation.AppNavHost
import com.gtg.electroshopandroid.ui.components.BottomBar
import com.gtg.electroshopandroid.ui.components.TopBar

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            AppNavHost(navController)
        }
    }
}
