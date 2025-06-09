package com.gtg.electroshopandroid.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreBottomSheet() {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        ExploreSheet(
            sheetState = sheetState,
            onDismiss = {
                scope.launch {
                    sheetState.hide()
                    showSheet = false
                }
            }
        )
    }

    // Nút để mở Bottom Sheet
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { showSheet = true }) {
            Text("Hiện Khám Phá")
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        // ✅ Bạn có thể thay nội dung này theo thiết kế của bạn
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
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
            }
    }
}

@Composable
fun ExploreSheetContent(onDismiss: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(16.dp),
        ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Khám phá", style = MaterialTheme.typography.titleLarge)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            // Nội dung khác...
        }
}



@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewExploreBottomSheet() {
    MaterialTheme {
        ExploreBottomSheet()
    }
}

