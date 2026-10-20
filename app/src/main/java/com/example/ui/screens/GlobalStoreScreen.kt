package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.WalletViewModel
import com.example.ui.components.DzBottomNavBar
import com.example.ui.components.DzTopBar
import com.example.ui.theme.DzBlack
import com.example.ui.theme.DzCardBackground
import com.example.ui.theme.DzCardBorder
import com.example.ui.theme.DzCardElevated
import com.example.ui.theme.DzDarkBackground
import com.example.ui.theme.DzGreenIncome
import com.example.ui.theme.DzTextMuted
import com.example.ui.theme.DzTextPrimary
import com.example.ui.theme.DzTextSecondary
import com.example.ui.theme.DzWhite

data class StoreItem(
    val name: String,
    val category: String,
    val paymentMode: String,
    val discount: String,
    val badgeColor: Color
)

@Composable
fun GlobalStoreScreen(viewModel: WalletViewModel) {
    BackHandler { viewModel.navigateBack() }

    val context = LocalContext.current
    val strings = viewModel.strings
    val currentTab by viewModel.currentTab.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("الكل") }

    val categories = listOf("الكل", "إلكترونيات", "أزياء", "مستحضرات", "المنزل")

    val stores = listOf(
        StoreItem("Amazon", "الكل", "دفع بـ USDT / بطاقة VISA", "كاش باك 3%", Color(0xFFFF9900)),
        StoreItem("eBay", "إلكترونيات", "دفع بـ USDT / بطاقة VISA", "عروض حصرية", Color(0xFF0064D2)),
        StoreItem("AliExpress", "الكل", "دفع بـ USDT / بطاقة VISA", "توصيل للجزائر", Color(0xFFFF4747)),
        StoreItem("Walmart", "المنزل", "دفع بـ USDT / بطاقة VISA", "أسعار مخفضة", Color(0xFF0071DC))
    )

    val filteredStores = stores.filter {
        (selectedCategory == "الكل" || it.category == selectedCategory || it.category == "الكل") &&
        (searchQuery.isBlank() || it.name.contains(searchQuery, ignoreCase = true))
    }

    Scaffold(
        containerColor = DzDarkBackground,
        bottomBar = {
            DzBottomNavBar(
                selectedTab = currentTab,
                onTabSelected = { viewModel.setTab(it) },
                strings = strings
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            DzTopBar(
                title = strings.globalShopping,
                onBackClick = { viewModel.setTab(0) }
            )

            // Search input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(strings.searchStoreHint, color = DzTextSecondary, fontSize = 12.sp) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = DzTextSecondary)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DzWhite,
                    unfocusedBorderColor = DzCardBorder,
                    focusedTextColor = DzTextPrimary,
                    unfocusedTextColor = DzTextPrimary,
                    focusedContainerColor = DzCardBackground,
                    unfocusedContainerColor = DzCardBackground
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Category Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) DzWhite else DzCardBackground)
                            .border(1.dp, DzCardBorder, RoundedCornerShape(20.dp))
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) DzBlack else DzTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Stores List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                items(filteredStores) { store ->
                    StoreCardRow(store = store, onShopClick = {
                        Toast.makeText(context, "جاري فتح متجر ${store.name} مع دفع مباشر بـ USDT", Toast.LENGTH_SHORT).show()
                    })
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun StoreCardRow(store: StoreItem, onShopClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DzCardBackground)
            .border(1.dp, DzCardBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onShopClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DzCardElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = store.name.take(2).uppercase(),
                        color = store.badgeColor,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = store.name,
                        color = DzWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = store.paymentMode,
                        color = DzTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(DzCardElevated)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = store.discount,
                    color = DzGreenIncome,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
