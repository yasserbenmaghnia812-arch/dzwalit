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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.WalletViewModel
import com.example.ui.components.DzPillButton
import com.example.ui.components.DzTopBar
import com.example.ui.theme.DzBlack
import com.example.ui.theme.DzCardBackground
import com.example.ui.theme.DzCardBorder
import com.example.ui.theme.DzCardElevated
import com.example.ui.theme.DzDarkBackground
import com.example.ui.theme.DzGreenIncome
import com.example.ui.theme.DzTextPrimary
import com.example.ui.theme.DzTextSecondary
import com.example.ui.theme.DzWhite

@Composable
fun BuyUsdtScreen(viewModel: WalletViewModel) {
    BackHandler { viewModel.navigateBack() }

    val context = LocalContext.current
    val strings = viewModel.strings
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Card, 1: Bank, 2: P2P
    var amountText by remember { mutableStateOf("100") }
    var selectedMethod by remember { mutableStateOf("بطاقة بنكية (Visa/Mastercard)") }

    val amount = amountText.toDoubleOrNull() ?: 100.0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DzDarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                DzTopBar(
                    title = strings.buyUsdt,
                    onBackClick = { viewModel.navigateBack() }
                )

                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Spacer(modifier = Modifier.height(14.dp))

                    // 3 Tabs: Card, Bank, P2P
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(DzCardBackground)
                            .border(1.dp, DzCardBorder, RoundedCornerShape(14.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf(strings.bankCard, strings.bankTransfer, strings.p2p).forEachIndexed { index, tabName ->
                            val isSelected = selectedTab == index
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) DzWhite else Color.Transparent)
                                    .clickable { selectedTab = index }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tabName,
                                    color = if (isSelected) DzBlack else DzTextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Amount section
                    Text(
                        text = "اختر المبلغ",
                        color = DzTextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        trailingIcon = {
                            Text(
                                text = "USDT",
                                color = DzTextSecondary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("buy_amount_input"),
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

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "≈ $${String.format("%.2f", amount)} USD (سعر حقيقي 1:1)",
                        color = DzTextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Payment Method selector
                    Text(
                        text = "اختر طريقة الدفع",
                        color = DzTextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(DzCardBackground)
                            .border(1.dp, DzCardBorder, RoundedCornerShape(14.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Row {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp, 16.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(Color(0xFF1A1F71)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("V", color = DzWhite, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp, 16.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(Color(0xFFEB001B)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("M", color = DzWhite, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = strings.bankCard,
                                        color = DzTextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "رسوم 2.5%",
                                        color = DzTextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selected",
                                tint = DzWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Bottom Notice & Buy Now Button
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = strings.securePaymentNotice,
                    color = DzTextSecondary,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(14.dp))

                DzPillButton(
                    text = strings.buyNow,
                    isLoading = isLoading,
                    onClick = {
                        viewModel.buyUsdt(
                            amount = amount,
                            paymentMethod = selectedMethod,
                            onSuccess = {
                                Toast.makeText(context, "تم شراء $amount USDT وإيداعها في رصيدك بنجاح", Toast.LENGTH_LONG).show()
                                viewModel.navigateBack()
                            }
                        )
                    }
                )
            }
        }
    }
}
