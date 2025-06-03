package com.gtg.electroshopandroid.data.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.gtg.electroshopandroid.R

sealed class Screen(val route: String, val icon: ImageVector, @StringRes val label: Int) {
    data object Home : Screen("home", Icons.Filled.Home, R.string.home)
    data object Explore : Screen("explore", Icons.Filled.Explore, R.string.explore)
    data object Cart : Screen("cart", Icons.Filled.ShoppingCart, R.string.cart)
    data object Favorites : Screen("favorites", Icons.Filled.Favorite, R.string.favorites)
    data object Profile : Screen("profile", Icons.Filled.Person, R.string.profile)
}

val bottomNavItems = listOf(
    Screen.Home, Screen.Explore, Screen.Cart, Screen.Favorites, Screen.Profile
)