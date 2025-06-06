package com.gtg.electroshopandroid.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gtg.electroshopandroid.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(
    onBackToProfile: () -> Unit,
    onChangeSuccess: () -> Unit,

) {
    val viewModel: ProfileDetailViewModel = viewModel()
    val userName by viewModel.userName
    val scrollState = rememberScrollState()


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "CHỈNH SỬA HỒ SƠ",
                        fontSize = 24.sp,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        // TODO: Xử lý quay lại ở đây, ví dụ: navController.popBackStack()
                        onBackToProfile()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White),
        ) {
            Column(
                modifier = Modifier
                    //.fillMaxWidth(0.8f)
                    .padding(horizontal = 32.dp)
                    .verticalScroll(scrollState), // enable scroll
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(100.dp)
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.08f))
                Text("Tên Đăng Nhập:", fontSize = 18.sp, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.fillMaxHeight(0.0078f))
                OutlinedTextField(
                    value = userName,
                    onValueChange = viewModel::onUserNameChanged,
                    placeholder = { Text("Điền tên đăng nhập của bạn...") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.08f))
                Text("Tên Đăng Nhập:", fontSize = 18.sp, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.fillMaxHeight(0.0078f))
                OutlinedTextField(
                    value = userName,
                    onValueChange = viewModel::onUserNameChanged,
                    placeholder = { Text("Điền tên đăng nhập của bạn...") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                Text("Tên Đăng Nhập:", fontSize = 18.sp, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.fillMaxHeight(0.0078f))
                OutlinedTextField(
                    value = userName,
                    onValueChange = viewModel::onUserNameChanged,
                    placeholder = { Text("Điền tên đăng nhập của bạn...") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                Text("Tên Đăng Nhập:", fontSize = 18.sp, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.fillMaxHeight(0.0078f))
                OutlinedTextField(
                    value = userName,
                    onValueChange = viewModel::onUserNameChanged,
                    placeholder = { Text("Điền tên đăng nhập của bạn...") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )

            }
        }
    }
}

@Preview
@Composable
fun PreviewSignUpScreen() {
    Box(
        modifier = Modifier
            .size(360.dp, 640.dp)
            .border(2.dp, Color.Black)
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        ProfileDetailScreen(onChangeSuccess = {}, onBackToProfile = {})
    }
}