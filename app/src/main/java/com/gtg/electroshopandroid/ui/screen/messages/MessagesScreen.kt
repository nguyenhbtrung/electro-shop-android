package com.gtg.electroshopandroid.ui.screen.messages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gtg.electroshopandroid.R
import com.gtg.electroshopandroid.ui.components.TopBarWithTitleAndBack
import com.gtg.electroshopandroid.ui.theme.ElectroShopAndroidTheme

@Composable
fun MessagesScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopBarWithTitleAndBack(
                title = R.string.msg_screen_title,
                onBack = onBack
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
