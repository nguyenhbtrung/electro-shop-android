package com.gtg.electroshopandroid.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.gtg.electroshopandroid.data.model.Screen
import com.gtg.electroshopandroid.navigation.AppNavHost
import com.gtg.electroshopandroid.ui.components.BottomBarMain
import com.gtg.electroshopandroid.ui.components.TopBarMain

@Composable
fun MainScreen(navController: NavHostController) {

    Scaffold(
        topBar = {
            TopBarMain(
                onMessagesClick = { navController.navigate(Screen.Messages.route) },
                onNotificationsClick = { navController.navigate(Screen.Notifications.route) }
            )
        },
        bottomBar = { BottomBarMain(navController) }
    ) { paddingValues ->
        Box(Modifier
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            AppNavHost(navController)
        }
    }
}
