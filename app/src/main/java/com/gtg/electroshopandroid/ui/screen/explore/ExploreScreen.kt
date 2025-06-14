package com.gtg.electroshopandroid.ui.screen.explore

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.gtg.electroshopandroid.R

@Composable
fun ExploreScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = stringResource(R.string.explore))
    }
}