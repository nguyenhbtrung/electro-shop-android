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

sealed class Screen(val route: String, val icon: ImageVector?, @StringRes val label: Int) {
    data object Home : Screen("home", Icons.Filled.Home, R.string.home)
    data object Explore : Screen("explore", Icons.Filled.Explore, R.string.explore)
    data object Cart : Screen("cart", Icons.Filled.ShoppingCart, R.string.cart)
    data object Favorites : Screen("favorites", Icons.Filled.Favorite, R.string.favorites)
    data object Profile : Screen("profile", Icons.Filled.Person, R.string.profile)

    data object Product : Screen("products/{id}", null, R.string.product_detail)
    data object Category : Screen("categories/{id}", null, R.string.categories)
    data object Messages : Screen("messages", null, R.string.msg)
    data object Notifications: Screen("notifications", null, R.string.notifications)

    data object OrderHistory: Screen("orderHistory", null, R.string.order_history)

    data object Login : Screen("login", null, R.string.login)
    data object Signup : Screen("signup", null, R.string.signup)

    data object ProfileDetail : Screen("profileDetail", null, R.string.profile_detail)
    data object Settings : Screen("settings", null, R.string.settings)
    data object BrowsingHistory : Screen("browsingHistory", null, R.string.browsing_history)
    data object Support : Screen("support", null, R.string.support)
    data object ReturnHistory : Screen("returnHistory", null, R.string.return_history)

    data object OrderDetail : Screen("order_detail/{orderId}", null, R.string.order_detail)
    data object CreateReturn : Screen("create_return/{orderId}", null, R.string.create_return)
}

val bottomNavItems = listOf(
    Screen.Home, Screen.Explore, Screen.Cart, Screen.Favorites, Screen.Profile
)