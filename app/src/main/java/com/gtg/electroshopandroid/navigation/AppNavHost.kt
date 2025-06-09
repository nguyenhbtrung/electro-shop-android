package com.gtg.electroshopandroid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gtg.electroshopandroid.data.model.Screen
import com.gtg.electroshopandroid.ui.screen.auth.LoginScreen
import com.gtg.electroshopandroid.ui.screen.auth.SignUpScreen
import com.gtg.electroshopandroid.ui.screen.cart.CartScreen
import com.gtg.electroshopandroid.ui.screen.favorites.FavoritesScreen
import com.gtg.electroshopandroid.ui.screen.home.HomeScreen
import com.gtg.electroshopandroid.ui.screen.messages.MessagesScreen
import com.gtg.electroshopandroid.ui.screen.notifications.NotificationsScreen
import com.gtg.electroshopandroid.ui.screen.returns.ReturnHistoryScreen
import com.gtg.electroshopandroid.ui.screen.returns.ReturnDetailScreen
import com.gtg.electroshopandroid.ui.screen.order.OrderHistoryScreen
import com.gtg.electroshopandroid.ui.screen.order.OrderDetailScreen
import com.gtg.electroshopandroid.ui.screen.order.CreateReturnScreen
import com.gtg.electroshopandroid.ui.screen.profile.ProfileDetailScreen
import com.gtg.electroshopandroid.ui.screen.profile.ProfileScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.gtg.electroshopandroid.ui.screen.product.ProductScreen
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.gtg.electroshopandroid.ElectroShopApplication
import com.gtg.electroshopandroid.ui.screen.category.CategoryScreen
import com.gtg.electroshopandroid.ui.screen.home.SearchResultsScreen
import com.gtg.electroshopandroid.ui.screen.order.OrderHistoryViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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

        composable(Screen.Home.route)     { HomeScreen(navController = navController) }
        composable(Screen.Cart.route)     { CartScreen() }
        composable(Screen.Favorites.route){ FavoritesScreen(navController = navController) }
        composable(Screen.Profile.route)  { ProfileScreen(navController) }

        composable(
            route = Screen.Product.route,
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("id") ?: return@composable
            ProductScreen(
                productId = productId,
                onCategoryClick = { id, name ->
                    val encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString())
                    navController.navigate("categories/$id/$encodedName")
                },
                onBack = { navController.popBackStack() },
                navController = navController
            )
        }

        composable(
            route = Screen.Category.route,
            arguments = listOf(navArgument("id") {type = NavType.IntType},
                navArgument("name") { type = NavType.StringType})
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("id") ?: return@composable
            val rawName = backStackEntry.arguments?.getString("name") ?: ""
            val categoryName = URLDecoder.decode(rawName, "UTF-8")
            CategoryScreen(
                categoryId = categoryId,
                categoryName = categoryName,
                onBack = { navController.popBackStack() },
                onProductClick = { navController.navigate("products/$it")}
            )
        }

        composable(
            route = "search_results/{query}",
            arguments = listOf(navArgument("query") { type = NavType.StringType })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            SearchResultsScreen(
                query = query,
                navController = navController ,
                onBack = { navController.popBackStack()})

        }

        composable(Screen.ProfileDetail.route) {
            ProfileDetailScreen(
                onBackToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onChangeSuccess = {
                    navController.navigate(Screen.Profile.route)
                }

            )
        }

        composable(Screen.OrderHistory.route) {
            OrderHistoryScreen(
                navController = navController,
                onBack = { navController.popBackStack() })
        }

        composable(Screen.Messages.route) {
            MessagesScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Notifications.route) {
            NotificationsScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.OrderDetail.route,
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt("orderId")
            OrderDetailScreen(
                orderId = orderId,
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ReturnHistory.route) {
            ReturnHistoryScreen(
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }


        composable(
            route = Screen.CreateReturn.route,
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: return@composable

            val context = LocalContext.current.applicationContext as ElectroShopApplication
            val orderHistoryRepository = context.container.orderHistoryRepository

            val viewModel: OrderHistoryViewModel = viewModel(
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return OrderHistoryViewModel(orderHistoryRepository) as T
                    }
                }
            )

            LaunchedEffect(orderId) {
                viewModel.loadOrders()
            }

            val orders by viewModel.orders.collectAsState()
            val orderDto = orders.find { it.orderId == orderId }

            if (orderDto != null) {
                CreateReturnScreen(
                    orderId = orderId,
                    orderDto = orderDto,
                    navController = navController,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(
            route = Screen.ReturnDetail.route,
            arguments = listOf(navArgument("returnId") { type = NavType.IntType })
        ) { backStackEntry ->
            val returnId = backStackEntry.arguments?.getInt("returnId") ?: return@composable
            ReturnDetailScreen(
                returnId = returnId,
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
