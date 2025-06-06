package com.gtg.electroshopandroid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gtg.electroshopandroid.data.model.Screen
import com.gtg.electroshopandroid.ui.screen.auth.LoginScreen
import com.gtg.electroshopandroid.ui.screen.auth.SignUpScreen
import com.gtg.electroshopandroid.ui.screen.cart.CartScreen
import com.gtg.electroshopandroid.ui.screen.explore.ExploreScreen
import com.gtg.electroshopandroid.ui.screen.favorites.FavoritesScreen
import com.gtg.electroshopandroid.ui.screen.home.HomeScreen
import com.gtg.electroshopandroid.ui.screen.messages.MessagesScreen
import com.gtg.electroshopandroid.ui.screen.notifications.NotificationsScreen
import com.gtg.electroshopandroid.ui.screen.order.OrderHistoryScreen
import com.gtg.electroshopandroid.ui.screen.profile.ProfileScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                onSignupClick = {
                    navController.navigate(Screen.Signup.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true } // Xóa màn login khỏi backstack
                    }
                }
            )
        }

        composable(Screen.Signup.route) {
            SignUpScreen(
                onBackToLogin = {
                    navController.popBackStack() // Quay lại màn login
                },
                onSignUpSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true } // Xóa màn login khỏi backstack
                    }
                }
            )
        }

        composable(Screen.Home.route)     { HomeScreen() }
        composable(Screen.Explore.route)  { ExploreScreen() }
        composable(Screen.Cart.route)     { CartScreen() }
        composable(Screen.Favorites.route){ FavoritesScreen() }
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        composable(Screen.OrderHistory.route) {
            OrderHistoryScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Messages.route) {
            MessagesScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Notifications.route) {
            NotificationsScreen(onBack = { navController.popBackStack() })
        }
    }
}
