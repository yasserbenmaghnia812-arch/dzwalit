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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.model.VirtualCardItem
import com.example.ui.AppScreen
import com.example.ui.WalletViewModel
import com.example.ui.components.DzBottomNavBar
import com.example.ui.components.DzPillButton
import com.example.ui.components.DzTopBar
import com.example.ui.components.DzVirtualCard
import com.example.ui.theme.DzBlack
import com.example.ui.theme.DzCardBackground
import com.example.ui.theme.DzCardBorder
import com.example.ui.theme.DzCardElevated
import com.example.ui.theme.DzDarkBackground
import com.example.ui.theme.DzGreenIncome
import com.example.ui.theme.DzRedExpense
import com.example.ui.theme.DzTextPrimary
import com.example.ui.theme.DzTextSecondary
import com.example.ui.theme.DzWhite

@Composable
fun CardsScreen(viewModel: WalletViewModel) {
    BackHandler { viewModel.navigateBack() }

    val context = LocalContext.current
    val strings = viewModel.strings
    val currentTab by viewModel.currentTab.collectAsState()
    val cards by viewModel.cards.collectAsState()
    val primaryCard = cards.firstOrNull() ?: VirtualCardItem()

    var showDetailsDialog by remember { mutableStateOf(false) }

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
                .padding(horizontal = 20.dp)
        ) {
            item {
                DzTopBar(
                    title = strings.useCard,
                    onBackClick = { viewModel.setTab(0) }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // The Virtual Card Component
                DzVirtualCard(
                    cardHolder = primaryCard.cardHolder,
                    last4 = primaryCard.last4,
                    expiry = primaryCard.expiryDate,
                    status = primaryCard.status,
                    availableBalance = primaryCard.availableBalance
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 3 Card Action Buttons: Details, Freeze, Settings
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    CardActionButton(
                        icon = Icons.Default.Info,
                        label = strings.cardDetails,
                        onClick = { showDetailsDialog = true }
                    )
                    CardActionButton(
                        icon = Icons.Default.AcUnit,
                        label = if (primaryCard.status == "active") strings.freezeCard else strings.unfreezeCard,
                        color = if (primaryCard.status == "active") DzRedExpense else DzGreenIncome,
                        onClick = {
                            viewModel.toggleCardFreeze(primaryCard)
                            val actionName = if (primaryCard.status == "active") "تم تجميد البطاقة" else "تم إلغاء تجميد البطاقة"
                            Toast.makeText(context, actionName, Toast.LENGTH_SHORT).show()
                        }
                    )
                    CardActionButton(
                        icon = Icons.Default.Settings,
                        label = strings.settings,
                        onClick = { viewModel.navigateTo(AppScreen.SECURITY) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Spending Limit Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DzCardBackground)
                        .border(1.dp, DzCardBorder, RoundedCornerShape(16.dp))
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(strings.spendingLimit, color = DzTextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "${String.format("%,.2f", primaryCard.spendingLimit)} USDT",
                                color = DzWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(DzCardElevated)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("شهرياً", color = DzTextSecondary, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Online Payment Toggle
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DzCardBackground)
                        .border(1.dp, DzCardBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(strings.onlinePayment, color = DzTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("تفعيل الشراء عبر الإنترنت ومتاجر الويب", color = DzTextSecondary, fontSize = 11.sp)
                        }
                        Switch(
                            checked = primaryCard.onlinePaymentEnabled,
                            onCheckedChange = { viewModel.toggleOnlinePayment(primaryCard) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = DzWhite,
                                checkedTrackColor = DzGreenIncome,
                                uncheckedThumbColor = DzTextSecondary,
                                uncheckedTrackColor = DzCardElevated
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Partner / Payment Acceptance Logos
                Text(
                    text = "الشبكات والمنصات المقبولة",
                    color = DzTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("Apple Pay", "Google Pay", "PayPal", "Amazon").forEach { partner ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(DzCardBackground)
                                .border(1.dp, DzCardBorder, RoundedCornerShape(10.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(partner, color = DzTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                DzPillButton(
                    text = strings.manageCard,
                    onClick = { Toast.makeText(context, "إدارة البطاقة مفعلة بالكامل", Toast.LENGTH_SHORT).show() }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Card Details Security Dialog
        if (showDetailsDialog) {
            AlertDialog(
                onDismissRequest = { showDetailsDialog = false },
                containerColor = DzCardElevated,
                title = { Text("بيانات بطاقة VISA الافتراضية", color = DzWhite, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("الاسم: ${primaryCard.cardHolder}", color = DzTextPrimary, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("رقم البطاقة: 4532 •••• •••• ${primaryCard.last4}", color = DzTextPrimary, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("تاريخ الانتهاء: ${primaryCard.expiryDate}", color = DzTextPrimary, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("رمز CVV: 742 (محمي ومشفر)", color = DzGreenIncome, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDetailsDialog = false }) {
                        Text("إغلاق", color = DzWhite)
                    }
                }
            )
        }
    }
}

@Composable
fun CardActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color = DzTextPrimary,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(DzCardBackground)
                .border(1.dp, DzCardBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            color = DzTextSecondary,
            fontSize = 11.sp
        )
    }
}
