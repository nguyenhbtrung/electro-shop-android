package com.gtg.electroshopandroid

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.gtg.electroshopandroid.preferences.TokenPreferences
import com.gtg.electroshopandroid.ui.example.ExampleScreen
import com.gtg.electroshopandroid.ui.screen.main.MainScreen
import com.gtg.electroshopandroid.ui.theme.ElectroShopAndroidTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ElectroShopAndroidTheme {
                ExampleScreen()
            }
        }

        // Test authentication api
//        val tokenPreferences = TokenPreferences(applicationContext)
//        lifecycleScope.launch {
//            tokenPreferences.updateAccessToken("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJuYW1laWQiOiIyZjEwODg3My0xNGQ3LTRmN2MtODE5Ny01NDJmYmNlY2MyNDIiLCJ1bmlxdWVfbmFtZSI6InVzZXIwMSIsInJvbGUiOiJVc2VyIiwibmJmIjoxNzQ5MTc0MzIwLCJleHAiOjE3NDk3NzkxMjAsImlhdCI6MTc0OTE3NDMyMCwiaXNzIjoiaHR0cHM6Ly9sb2NhbGhvc3Q6NzE2OSIsImF1ZCI6Imh0dHBzOi8vbG9jYWxob3N0OjcxNjkifQ.2FsTLHPGTAYxPPbmdT7cr9EuLPj9bmU0RpXlEqfXE5YSE4LzaCLOD0MnHufj1JD737gsT5juy3l6jdF3II8ZsQ")
//            setContent {
//                ElectroShopAndroidTheme {
//                    ExampleScreen() // Test call api
//                }
//            }
//        }
    }
}

@Composable
fun TestThemeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(modifier = Modifier.padding(8.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Test",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Test",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {}
            ) {
                Text(
                    text = "Test"
                )
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text(text = "Test")
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Text(text = "Test")
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ElectroShopAndroidTheme(darkTheme = false) {
        MainScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun TestThemePreview() {
    ElectroShopAndroidTheme(darkTheme = false) {
        TestThemeScreen()
    }
}