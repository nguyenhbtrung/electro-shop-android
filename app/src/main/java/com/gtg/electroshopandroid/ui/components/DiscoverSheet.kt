package com.gtg.electroshopandroid.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.NetworkWifi
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.FlowRow // <-- Đảm bảo bạn dùng compose.foundation 1.4.0+ hoặc Material3

// Với Material 3 Modal Bottom Sheet
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DiscoverSheet(
    modifier: Modifier = Modifier,
    categories: List<String> = listOf("PC", "Laptop", "CPU"),
    subCategories: List<String> = listOf("All", "Laptop WorkStation", "Laptop Gaming", "Laptop Ultrabook", "Chromebook", "MacBook"),
    brands: List<Pair<String, ImageVector>> = listOf(
        "Asus" to Icons.Default.Laptop,
        "Samsung" to Icons.Default.PhoneAndroid,
        "Apple" to Icons.Default.PhoneIphone,
        "HP" to Icons.Default.Computer,
        "Dell" to Icons.Default.NetworkWifi
    )
) {
    // State
    var selectedCategory by remember { mutableStateOf(categories.first()) }
    val selectedSub = remember { mutableStateListOf<String>() }

    Column(
        modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Khám phá", style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = { /* đóng sheet */ }) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }

        Spacer(Modifier.height(16.dp))

        // Segmented Control
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            categories.forEach { cat ->
                OutlinedButton(
                    onClick = { selectedCategory = cat },
                    colors = if (selectedCategory == cat) ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ) else ButtonDefaults.outlinedButtonColors(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(cat,
                        color = if (selectedCategory == cat)
                            MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Filter Chips
        FlowRow {
            subCategories.forEachIndexed { index, sub ->
                FilterChip(
                    selected = selectedSub.contains(sub),
                    onClick = {
                        if (selectedSub.contains(sub)) selectedSub.remove(sub)
                        else selectedSub.add(sub)
                    },
                    label = { Text(sub) },
                    modifier = Modifier
                        .padding(end = 8.dp, bottom = 8.dp) // spacing thủ công
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Brands
        Text("Thương hiệu", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            brands.forEach { (name, icon) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { /* filter theo brand */ }
                ) {
                    Icon(icon, contentDescription = name, modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(4.dp))
                    Text(name, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

// Nếu bạn muốn hiển thị trong một Bottom Sheet:
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenWithDiscover() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = { /* đóng sheet */ },
        sheetState = sheetState,
    ) {
        DiscoverSheet()
    }

    // Phần UI chính của bạn đặt ở đây…
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDiscoverSheet() {
    MaterialTheme {
        Surface {
            DiscoverSheet(
                categories = listOf("PC", "Laptop", "CPU"),
                subCategories = listOf(
                    "All", "Laptop WorkStation", "Laptop Gaming",
                    "Laptop Ultrabook", "Chromebook", "MacBook"
                ),
                brands = listOf(
                    "Asus" to Icons.Default.Laptop,
                    "Samsung" to Icons.Default.PhoneAndroid,
                    "Apple" to Icons.Default.PhoneIphone,
                    "HP" to Icons.Default.Computer,
                    "Dell" to Icons.Default.NetworkWifi
                )
            )
        }
    }
}

