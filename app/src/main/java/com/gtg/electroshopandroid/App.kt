package com.gtg.electroshopandroid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gtg.electroshopandroid.data.model.Screen
import com.gtg.electroshopandroid.navigation.AppNavHost
import com.gtg.electroshopandroid.ui.screen.main.MainScreen

@Composable
fun App() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isMainScreen = currentRoute in listOf(
        Screen.Home.route,
        Screen.Explore.route,
        Screen.Cart.route,
        Screen.Favorites.route,
        Screen.Profile.route
    )

    if (isMainScreen) {
        MainScreen(navController = navController)
    } else {
        AppNavHost(navController = navController)
    }

}