package com.gtg.electroshopandroid.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.gtg.electroshopandroid.data.model.Screen
import com.gtg.electroshopandroid.navigation.AppNavHost
import com.gtg.electroshopandroid.ui.components.BottomBarMain
import com.gtg.electroshopandroid.ui.components.ExploreSheetContent
import com.gtg.electroshopandroid.ui.components.TopBarMain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val mainViewModel: MainViewModel = viewModel()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            dragHandle = null
        ) {
            ExploreSheetContent(
                onDismiss = { showSheet = false },
                onParentCategoryClick = mainViewModel::setSelectedCategoryParent,
                onChildCategoryClick = mainViewModel::setSelectedCategoryChild,
                selectedCategoryParentId = mainViewModel.uiState.selectedCategoryParentId,
                selectedCategoryChildId = mainViewModel.uiState.selectedCategoryChildId,
            )
        }

        // Launch sheet.show() chỉ khi sheet đã được render
        LaunchedEffect(Unit) {
            sheetState.show()
        }
    }


    Scaffold(
        topBar = {
            TopBarMain(
                onMessagesClick = { navController.navigate(Screen.Messages.route) },
                onNotificationsClick = { navController.navigate(Screen.Notifications.route) }
            )
        },
        bottomBar = { BottomBarMain(navController, { showSheet = true }) }
    ) { paddingValues ->
        Box(Modifier
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            AppNavHost(navController)
        }
    }
}
