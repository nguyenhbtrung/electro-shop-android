package com.gtg.electroshopandroid.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.gtg.electroshopandroid.R
import com.gtg.electroshopandroid.data.model.Screen
import com.gtg.electroshopandroid.preferences.TokenPreferences
import kotlinx.coroutines.launch

@Composable
fun CategoryItem(
    title: String,
    leadingIcon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 10.dp, horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = Color.Black
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                color = Color.Black
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Arrow",
            tint = Color.Black
        )
    }
}

@Composable
fun CategoryItem2(
    title: String,
    leadingIcon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = Color.Red
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Red,
                fontSize = 18.sp
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Arrow",
            tint = Color.Red
        )
    }
}

@Composable
fun ProfileScreen(navController: NavHostController) {
    val viewModel: ProfileDetailViewModel = viewModel(factory = ProfileDetailViewModel.Factory)
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    val fullName by viewModel.fullName
    val email by viewModel.email
    val avatarUrl by viewModel.avatarImg

    val categories = listOf(
        Triple("Hồ sơ cá nhân", Icons.Default.Person, Screen.ProfileDetail.route),
        Triple("Đơn hàng của bạn", Icons.Default.AddShoppingCart, Screen.OrderHistory.route),
        Triple("Cài đặt", Icons.Default.Settings, Screen.Settings.route),
        Triple("Lịch sử hoàn trả", Icons.Default.Replay, Screen.ReturnHistory.route),
        Triple("Hỗ trợ khách hàng", Icons.Default.SupportAgent, Screen.Support.route),
        Triple("Thông báo", Icons.Default.Notifications, Screen.Notifications.route)
    )
    val categories2 = listOf(
        "Đăng xuất" to Icons.AutoMirrored.Filled.ExitToApp,
    )

    val context = LocalContext.current
    val tokenPrefs = remember { TokenPreferences(context) }
    val scope = rememberCoroutineScope()


    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Avatar",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(100.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = fullName,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = email,
                color = Color.Gray,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp)) // bo tròn
                    .background(Color.White) // màu khác với background
            ) {
                categories.forEachIndexed { index, (title, icon, route) ->
                    val topPadding = if (index == 0) 8.dp else 0.dp
                    val bottomPadding = if (index == categories.lastIndex) 8.dp else 0.dp
                    CategoryItem(
                        title = title,
                        leadingIcon = icon,
                        onClick = {
                            navController.navigate(route)
                        },
                        modifier = Modifier
                            .padding(top = topPadding, bottom = bottomPadding)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp)) // bo tròn
                    .background(Color.White) // màu khác với bazckground
            ) {
                categories2.forEachIndexed { index, (title, icon) ->
                    val topPadding = if (index == 0) 8.dp else 0.dp
                    val bottomPadding = if (index == categories2.lastIndex) 8.dp else 0.dp
                    CategoryItem2(
                        title = title,
                        leadingIcon = icon,
                        onClick = {
                            if (title == "Đăng xuất") {
                                scope.launch {
                                    tokenPrefs.clearAccessToken()
                                    // Chuyển hướng về màn hình đăng nhập (bạn cần đặt lại tên route đúng nếu khác)
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(top = topPadding, bottom = bottomPadding)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewProfileScreen(){
    Box(
        modifier = Modifier
            .size(360.dp, 640.dp)
            .border(2.dp, Color.Black)
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        val navController = rememberNavController()
        ProfileScreen(navController = navController)
    }
}