package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.TransactionItem
import com.example.ui.AppScreen
import com.example.ui.WalletViewModel
import com.example.ui.components.DzBottomNavBar
import com.example.ui.components.DzRoundActionButton
import com.example.ui.theme.DzBlack
import com.example.ui.theme.DzCardBackground
import com.example.ui.theme.DzCardBorder
import com.example.ui.theme.DzCardElevated
import com.example.ui.theme.DzDarkBackground
import com.example.ui.theme.DzGreenBadge
import com.example.ui.theme.DzGreenIncome
import com.example.ui.theme.DzRedExpense
import com.example.ui.theme.DzTextMuted
import com.example.ui.theme.DzTextPrimary
import com.example.ui.theme.DzTextSecondary
import com.example.ui.theme.DzWhite

@Composable
fun HomeScreen(viewModel: WalletViewModel) {
    val strings = viewModel.strings
    val currentTab by viewModel.currentTab.collectAsState()
    val isExpanded by viewModel.isBalanceExpanded.collectAsState()
    val profile by viewModel.userProfile.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val cards by viewModel.cards.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    val unreadCount = notifications.count { !it.read }
    val primaryCard = cards.firstOrNull()

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 18.dp)
        ) {
            // Header: DZ Wallet logo + notification & user avatar
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.dz_wallet_icon_1792521385553),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "DZ Wallet",
                            color = DzWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Language switcher
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(DzCardBackground)
                                .border(1.dp, DzCardBorder, RoundedCornerShape(12.dp))
                                .clickable { viewModel.toggleLanguage() }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (viewModel.currentLanguage.collectAsState().value == "ar") "FR" else "عربي",
                                color = DzTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Notification Icon with Badge
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(DzCardBackground)
                                .border(1.dp, DzCardBorder, CircleShape)
                                .clickable { viewModel.navigateTo(AppScreen.NOTIFICATIONS) }
                                .testTag("home_notifications_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            BadgedBox(
                                badge = {
                                    if (unreadCount > 0) {
                                        Badge(containerColor = DzGreenIncome) {
                                            Text(unreadCount.toString(), color = DzBlack, fontSize = 9.sp)
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = DzTextPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // User Avatar
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(DzCardElevated)
                                .border(1.dp, DzCardBorder, CircleShape)
                                .clickable { viewModel.navigateTo(AppScreen.PROFILE) }
                                .testTag("home_profile_avatar"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (profile?.displayName?.firstOrNull() ?: 'A').uppercase(),
                                color = DzWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // USDT Balance Card (Foldable / Expandable)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(DzCardBackground)
                        .border(1.dp, DzCardBorder, RoundedCornerShape(20.dp))
                        .clickable { viewModel.toggleBalanceExpanded() }
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Green Tether USDT Badge
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(DzGreenIncome),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "₮",
                                        color = DzBlack,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 17.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "USDT (TRC20)",
                                    color = DzTextSecondary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Icon(
                                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Toggle Balance",
                                tint = DzTextSecondary
                            )
                        }

                        AnimatedVisibility(visible = isExpanded) {
                            Column {
                                Spacer(modifier = Modifier.height(14.dp))
                                val balance = profile?.usdtBalance ?: 1245.32
                                Text(
                                    text = String.format("%,.2f", balance),
                                    color = DzWhite,
                                    fontSize = 34.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = String.format("≈ $%,.2f USD", balance),
                                    color = DzTextSecondary,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 4 Circular Action Buttons: Send, Receive, Transfer, Buy
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DzRoundActionButton(
                        icon = Icons.Default.ArrowOutward,
                        label = strings.send,
                        onClick = { viewModel.navigateTo(AppScreen.SEND) }
                    )
                    DzRoundActionButton(
                        icon = Icons.Default.ArrowDownward,
                        label = strings.receive,
                        onClick = { viewModel.navigateTo(AppScreen.RECEIVE) }
                    )
                    DzRoundActionButton(
                        icon = Icons.Default.Refresh,
                        label = strings.transfer,
                        onClick = { viewModel.navigateTo(AppScreen.EXCHANGE) }
                    )
                    DzRoundActionButton(
                        icon = Icons.Default.ShoppingCart,
                        label = strings.buy,
                        onClick = { viewModel.navigateTo(AppScreen.BUY) }
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Mini Virtual VISA Card preview
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DzCardBackground)
                        .border(1.dp, DzCardBorder, RoundedCornerShape(16.dp))
                        .clickable { viewModel.navigateTo(AppScreen.CARDS) }
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
                                    .size(44.dp, 30.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF1E1E26))
                                    .border(1.dp, DzCardBorder, RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "VISA",
                                    color = DzWhite,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = strings.myVirtualCard,
                                    color = DzTextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "•••• ${primaryCard?.last4 ?: "4821"}",
                                    color = DzTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(DzGreenBadge)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = strings.validStatus,
                                color = DzGreenIncome,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Recent Transactions Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = strings.recentTransactions,
                        color = DzWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = strings.all,
                        color = DzTextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { viewModel.navigateTo(AppScreen.TRANSACTIONS) }
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Transaction items list
            if (transactions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "لا توجد معاملات بعد",
                            color = DzTextMuted,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                items(transactions.take(5)) { tx ->
                    TransactionRowItem(tx = tx)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun TransactionRowItem(tx: TransactionItem) {
    val isIncoming = tx.type in listOf("receive", "buy")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DzCardBackground)
            .border(1.dp, DzCardBorder, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(DzCardElevated),
                    contentAlignment = Alignment.Center
                ) {
                    val iconChar = when (tx.type) {
                        "receive" -> "↓"
                        "send" -> "↑"
                        "buy" -> "₮"
                        "exchange" -> "⇄"
                        else -> "💳"
                    }
                    Text(
                        text = iconChar,
                        color = if (isIncoming) DzGreenIncome else DzWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    val title = when (tx.type) {
                        "receive" -> "استلام من ${tx.sender.ifBlank { "Binance" }}"
                        "send" -> "دفع إلى ${tx.recipient.ifBlank { "علي" }}"
                        "buy" -> "شراء USDT"
                        "exchange" -> "تحويل عملات"
                        else -> "شراء من ${tx.recipient.ifBlank { "Amazon" }}"
                    }
                    Text(
                        text = title,
                        color = DzTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${tx.network} • ${tx.status}",
                        color = DzTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                val sign = if (isIncoming) "+ " else "- "
                Text(
                    text = "$sign${String.format("%.2f", tx.amount)} ${tx.currency}",
                    color = if (isIncoming) DzGreenIncome else DzRedExpense,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "اليوم 12:45",
                    color = DzTextMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}
