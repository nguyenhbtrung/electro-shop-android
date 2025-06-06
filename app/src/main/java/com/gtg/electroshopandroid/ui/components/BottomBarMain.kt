package com.gtg.electroshopandroid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gtg.electroshopandroid.data.model.Screen
import com.gtg.electroshopandroid.data.model.bottomNavItems
import com.gtg.electroshopandroid.preferences.TokenPreferences

@Composable
fun BottomBarMain(
    navController: NavHostController,
    tokenPreferences: TokenPreferences = TokenPreferences(LocalContext.current)
) {
    val accessToken by tokenPreferences.accessTokenFlow.collectAsState(initial = null)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        bottomNavItems.forEach { screen ->
            NavigationBarItem(
                icon = {
                    if (screen.route == Screen.Cart.route) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = screen.icon ?: Icons.Filled.QuestionMark,
                                contentDescription = stringResource(id = screen.label),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    } else {
                        Icon(
                            imageVector = screen.icon ?: Icons.Filled.QuestionMark,
                            contentDescription = stringResource(id = screen.label)
                        )
                    }
                },
                label = {
                    if (screen.route != Screen.Cart.route) {
                        Text(text = stringResource(id = screen.label))
                    }
                },
                selected = currentRoute == screen.route,
                onClick = {
                    if (screen.route == Screen.Profile.route && accessToken == null) {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    } else if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            // Pop up để tránh stack quá sâu
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

