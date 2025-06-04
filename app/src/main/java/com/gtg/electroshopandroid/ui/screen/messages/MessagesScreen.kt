package com.gtg.electroshopandroid.ui.screen.messages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gtg.electroshopandroid.R
import com.gtg.electroshopandroid.ui.theme.ElectroShopAndroidTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.msg_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.msg))
        }
    }
}

@Preview
@Composable
fun PreviewMessagesScreen() {
    ElectroShopAndroidTheme {
        MessagesScreen(onBack = {})
    }
}
