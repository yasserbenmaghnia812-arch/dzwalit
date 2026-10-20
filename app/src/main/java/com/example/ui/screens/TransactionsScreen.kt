package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TransactionItem
import com.example.ui.WalletViewModel
import com.example.ui.components.DzBottomNavBar
import com.example.ui.components.DzTopBar
import com.example.ui.theme.DzBlack
import com.example.ui.theme.DzCardBackground
import com.example.ui.theme.DzCardBorder
import com.example.ui.theme.DzDarkBackground
import com.example.ui.theme.DzTextMuted
import com.example.ui.theme.DzTextPrimary
import com.example.ui.theme.DzTextSecondary
import com.example.ui.theme.DzWhite

@Composable
fun TransactionsScreen(viewModel: WalletViewModel) {
    BackHandler { viewModel.navigateBack() }

    val strings = viewModel.strings
    val currentTab by viewModel.currentTab.collectAsState()
    val allTransactions by viewModel.transactions.collectAsState()

    var selectedFilter by remember { mutableStateOf("الكل") }
    var searchQuery by remember { mutableStateOf("") }

    val filterOptions = listOf("الكل", "إرسال", "استقبال", "شراء", "دفع")

    val filteredTransactions = allTransactions.filter { tx ->
        val matchesFilter = when (selectedFilter) {
            "إرسال" -> tx.type == "send"
            "استقبال" -> tx.type == "receive"
            "شراء" -> tx.type == "buy"
            "دفع" -> tx.type in listOf("card_payment", "exchange")
            else -> true
        }
        val matchesSearch = if (searchQuery.isBlank()) true else {
            tx.recipient.contains(searchQuery, ignoreCase = true) ||
            tx.sender.contains(searchQuery, ignoreCase = true) ||
            tx.note.contains(searchQuery, ignoreCase = true) ||
            tx.txId.contains(searchQuery, ignoreCase = true)
        }
        matchesFilter && matchesSearch
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
                title = strings.transactions,
                onBackClick = { viewModel.navigateBack() }
            )

            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("بحث في المعاملات أو TxID", color = DzTextSecondary, fontSize = 12.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = DzTextSecondary
                    )
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

            // Filter Tabs
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterOptions) { tab ->
                    val isSelected = selectedFilter == tab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) DzWhite else DzCardBackground)
                            .border(1.dp, DzCardBorder, RoundedCornerShape(20.dp))
                            .clickable { selectedFilter = tab }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = tab,
                            color = if (isSelected) DzBlack else DzTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Transactions list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                if (filteredTransactions.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("لا توجد نتائج مطابقة", color = DzTextMuted, fontSize = 13.sp)
                        }
                    }
                } else {
                    items(filteredTransactions) { tx ->
                        TransactionRowItem(tx = tx)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}
