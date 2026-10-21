package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TransactionItem
import com.example.ui.WalletViewModel
import com.example.ui.components.DzBottomNavBar
import com.example.ui.components.DzPillButton
import com.example.ui.components.DzTopBar
import com.example.ui.theme.DzBlack
import com.example.ui.theme.DzCardBackground
import com.example.ui.theme.DzCardBorder
import com.example.ui.theme.DzCardElevated
import com.example.ui.theme.DzDarkBackground
import com.example.ui.theme.DzGreenBadge
import com.example.ui.theme.DzGreenIncome
import com.example.ui.theme.DzRedBadge
import com.example.ui.theme.DzRedExpense
import com.example.ui.theme.DzTextMuted
import com.example.ui.theme.DzTextPrimary
import com.example.ui.theme.DzTextSecondary
import com.example.ui.theme.DzWhite

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(viewModel: WalletViewModel) {
    BackHandler { viewModel.navigateBack() }

    val context = LocalContext.current
    val strings = viewModel.strings
    val currentTab by viewModel.currentTab.collectAsState()
    val allTransactions by viewModel.transactions.collectAsState()

    var selectedFilter by remember { mutableStateOf("الكل") }
    var searchQuery by remember { mutableStateOf("") }
    var selectedTxForDetails by remember { mutableStateOf<TransactionItem?>(null) }
    var showNewTxDialog by remember { mutableStateOf(false) }

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
            tx.txId.contains(searchQuery, ignoreCase = true) ||
            tx.currency.contains(searchQuery, ignoreCase = true)
        }
        matchesFilter && matchesSearch
    }

    // Totals calculation
    val totalIncome = allTransactions.filter { it.type in listOf("receive", "buy") }.sumOf { it.amount }
    val totalExpense = allTransactions.filter { it.type !in listOf("receive", "buy") }.sumOf { it.amount }

    Scaffold(
        containerColor = DzDarkBackground,
        bottomBar = {
            DzBottomNavBar(
                selectedTab = currentTab,
                onTabSelected = { viewModel.setTab(it) },
                strings = strings
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNewTxDialog = true },
                containerColor = DzGreenIncome,
                contentColor = DzBlack,
                shape = CircleShape,
                modifier = Modifier.testTag("add_transaction_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
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

            // Financial Summary Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DzCardBackground)
                    .border(1.dp, DzCardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("إجمالي الوارد", color = DzTextSecondary, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "+${String.format("%,.2f", totalIncome)}",
                            color = DzGreenIncome,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(DzCardBorder)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("إجمالي الصادر", color = DzTextSecondary, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "-${String.format("%,.2f", totalExpense)}",
                            color = DzRedExpense,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(DzCardBorder)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("عدد المعاملات", color = DzTextSecondary, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "${allTransactions.size}",
                            color = DzWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("بحث في المعاملات، الاسم أو TxID", color = DzTextSecondary, fontSize = 12.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = DzTextSecondary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .testTag("transactions_search_bar"),
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

            Spacer(modifier = Modifier.height(12.dp))

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
                            .padding(horizontal = 16.dp, vertical = 7.dp)
                            .testTag("tx_filter_$tab")
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

            Spacer(modifier = Modifier.height(14.dp))

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
                            Text("لا توجد معاملات مسجلة", color = DzTextMuted, fontSize = 13.sp)
                        }
                    }
                } else {
                    items(filteredTransactions) { tx ->
                        Box(modifier = Modifier.clickable { selectedTxForDetails = tx }) {
                            TransactionRowItem(tx = tx)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

        // Details Modal Bottom Sheet
        if (selectedTxForDetails != null) {
            val tx = selectedTxForDetails!!
            val isIncoming = tx.type in listOf("receive", "buy")
            ModalBottomSheet(
                onDismissRequest = { selectedTxForDetails = null },
                containerColor = DzCardElevated,
                sheetState = rememberModalBottomSheetState()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("تفاصيل المعاملة المالية", color = DzWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isIncoming) DzGreenBadge else DzRedBadge)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (tx.status == "confirmed") "مؤكدة بالكامل ✓" else "قيد المعالجة",
                                color = if (isIncoming) DzGreenIncome else DzRedExpense,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Amount Hero Display
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(DzCardBackground)
                            .padding(18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${if (isIncoming) "+" else "-"} ${String.format("%.2f", tx.amount)} ${tx.currency}",
                                color = if (isIncoming) DzGreenIncome else DzRedExpense,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "الشبكة: ${tx.network} • الرسوم: ${tx.fee} USDT",
                                color = DzTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Transaction Metadata Details
                    DetailRowItem(label = "نوع العملية", value = when (tx.type) {
                        "send" -> "إرسال USDT"
                        "receive" -> "استلام USDT"
                        "buy" -> "شراء عبر البطاقة"
                        "exchange" -> "تحويل بين العملات"
                        else -> "دفع إلكتروني متجر"
                    })
                    DetailRowItem(label = "المرسل / المستلم", value = if (isIncoming) tx.sender.ifBlank { "Binance" } else tx.recipient.ifBlank { "علي" })
                    DetailRowItem(label = "التاريخ والوقت", value = "اليوم، 12:45 UTC")
                    DetailRowItem(label = "معرف البلوك تشين (TxID)", value = tx.txId.ifBlank { "0x7a89b4f2c9081e" }, isCopyable = true, onCopy = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("TxID", tx.txId))
                        Toast.makeText(context, "تم نسخ معرف المعاملة TxID", Toast.LENGTH_SHORT).show()
                    })

                    Spacer(modifier = Modifier.height(20.dp))

                    // Action Buttons: View in TronScan Explorer & Share Receipt
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(DzCardBackground)
                                .border(1.dp, DzCardBorder, RoundedCornerShape(24.dp))
                                .clickable {
                                    val explorerUrl = if (tx.network.contains("ERC")) {
                                        "https://etherscan.io/tx/${tx.txId}"
                                    } else {
                                        "https://tronscan.org/#/transaction/${tx.txId}"
                                    }
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(explorerUrl))
                                    context.startActivity(intent)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, tint = DzWhite, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("مستكشف البلوك تشين", color = DzWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(DzGreenIncome)
                                .clickable {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, "إيصال معاملة DZ Wallet:\nالمبلغ: ${tx.amount} ${tx.currency}\nالحالة: ${tx.status}\nTxID: ${tx.txId}\nالشبكة: ${tx.network}")
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "مشاركة إيصال العملية"))
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Share, contentDescription = null, tint = DzBlack, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("مشاركة الإيصال", color = DzBlack, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Add Transaction Dialog
        if (showNewTxDialog) {
            var newRecipient by remember { mutableStateOf("") }
            var newAmount by remember { mutableStateOf("50") }
            var newType by remember { mutableStateOf("send") }

            AlertDialog(
                onDismissRequest = { showNewTxDialog = false },
                containerColor = DzCardElevated,
                title = { Text("تسجيل معاملة حقيقية جديدة", color = DzWhite, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("النوع: ${if (newType == "send") "إرسال USDT" else "استلام USDT"}", color = DzTextSecondary, fontSize = 12.sp)
                        Row(modifier = Modifier.padding(vertical = 8.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (newType == "send") DzWhite else DzCardBackground)
                                    .clickable { newType = "send" }
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text("إرسال", color = if (newType == "send") DzBlack else DzWhite, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (newType == "receive") DzWhite else DzCardBackground)
                                    .clickable { newType = "receive" }
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text("استلام", color = if (newType == "receive") DzBlack else DzWhite, fontSize = 12.sp)
                            }
                        }

                        OutlinedTextField(
                            value = newRecipient,
                            onValueChange = { newRecipient = it },
                            label = { Text("المستلم / الحساب", color = DzTextSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DzWhite,
                                unfocusedBorderColor = DzCardBorder,
                                focusedTextColor = DzTextPrimary,
                                unfocusedTextColor = DzTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newAmount,
                            onValueChange = { newAmount = it },
                            label = { Text("المبلغ (USDT)", color = DzTextSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DzWhite,
                                unfocusedBorderColor = DzCardBorder,
                                focusedTextColor = DzTextPrimary,
                                unfocusedTextColor = DzTextPrimary
                            )
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val amt = newAmount.toDoubleOrNull() ?: 50.0
                            showNewTxDialog = false
                            if (newType == "send") {
                                viewModel.sendUsdt(newRecipient.ifBlank { "علي" }, amt, "TRC20", "معاملة جديدة", onSuccess = {})
                            } else {
                                viewModel.buyUsdt(amt, newRecipient.ifBlank { "Binance" }, onSuccess = {})
                            }
                            Toast.makeText(context, "تمت إضافة المعاملة وحفظها في قاعدة بيانات السجل", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text("حفظ وتسجيل", color = DzGreenIncome, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showNewTxDialog = false }) {
                        Text("إلغاء", color = DzTextSecondary)
                    }
                }
            )
        }
    }
}

@Composable
fun DetailRowItem(
    label: String,
    value: String,
    isCopyable: Boolean = false,
    onCopy: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = DzTextSecondary, fontSize = 12.sp)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                color = DzWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            if (isCopyable && onCopy != null) {
                Spacer(modifier = Modifier.width(6.dp))
                IconButton(onClick = onCopy, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = DzGreenIncome,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
