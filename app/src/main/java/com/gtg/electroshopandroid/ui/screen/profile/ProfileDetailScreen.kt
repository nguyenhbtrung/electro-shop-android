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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import coil.compose.AsyncImage
import com.gtg.electroshopandroid.R
import com.gtg.electroshopandroid.data.repository.ProfileRepository
import com.gtg.electroshopandroid.ui.screen.product.ProductViewModel
import com.gtg.electroshopandroid.ui.theme.ElectroShopAndroidTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(
    onBackToProfile: () -> Unit,
    onChangeSuccess: () -> Unit,
) {
    val viewModel: ProfileDetailViewModel = viewModel(factory = ProfileDetailViewModel.Factory)
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }
    val userName by viewModel.userName
    val email by viewModel.email
    val fullName by viewModel.fullName
    val phoneNumber by viewModel.phoneNumber
    val address by viewModel.address
    val avatarUrl by viewModel.avatarImg
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(100.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text("Tên Đăng Nhập:", fontSize = 18.sp, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextField(
                    value = userName,
                    onValueChange = {},
                    placeholder = { Text("Tên đăng nhập") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text("Email:", fontSize = 18.sp, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Email") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text("Tên Người Dùng:", fontSize = 18.sp, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextField(
                    value = fullName,
                    onValueChange = viewModel::onFullNameChanged,
                    placeholder = { Text("Tên người dùng") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text("Số điện thoại:", fontSize = 18.sp, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = viewModel::onPhoneNumberChanged,
                    placeholder = { Text("Số điện thoại") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text("Địa chỉ:", fontSize = 18.sp, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = viewModel::onAddressChange,
                    placeholder = { Text("Địa chỉ") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { viewModel.onChangeClick(onChangeSuccess) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Cập nhật", fontSize = 20.sp)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewSignUpScreen() {
    Box(
        modifier = Modifier
            .size(360.dp, 1000.dp)
            .border(2.dp, Color.Black)
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        ElectroShopAndroidTheme {
        }
    }
}