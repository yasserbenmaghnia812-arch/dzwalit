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
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.ui.theme.DzGoldAccent
import com.example.ui.theme.DzGreenIncome
import com.example.ui.theme.DzTextPrimary
import com.example.ui.theme.DzTextSecondary
import com.example.ui.theme.DzWhite

@Composable
fun ExchangeScreen(viewModel: WalletViewModel) {
    BackHandler { viewModel.navigateBack() }

    val context = LocalContext.current
    val strings = viewModel.strings
    val isLoading by viewModel.isLoading.collectAsState()

    var fromAmountText by remember { mutableStateOf("100") }
    val btcRate = 0.000037 // 1 USDT = 0.000037 BTC
    val fromAmount = fromAmountText.toDoubleOrNull() ?: 100.0
    val calculatedBtc = fromAmount * btcRate

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
                    title = strings.currencyExchange,
                    onBackClick = { viewModel.navigateBack() }
                )

                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // "من" (From: USDT TRC20)
                    Text(strings.from, color = DzTextSecondary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))

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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(DzGreenIncome),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("₮", color = DzBlack, fontSize = 16.sp, fontWeight = FontWeight.Black)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("USDT (TRC20)", color = DzWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text("الرصيد: 1,245.32", color = DzTextSecondary, fontSize = 11.sp)
                                }
                            }

                            OutlinedTextField(
                                value = fromAmountText,
                                onValueChange = { fromAmountText = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .width(120.dp)
                                    .testTag("exchange_from_input"),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = DzWhite,
                                    unfocusedBorderColor = DzCardBorder,
                                    focusedTextColor = DzTextPrimary,
                                    unfocusedTextColor = DzTextPrimary,
                                    focusedContainerColor = DzCardElevated,
                                    unfocusedContainerColor = DzCardElevated
                                ),
                                singleLine = true
                            )
                        }
                    }

                    // Swap Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(DzCardElevated)
                                .border(1.dp, DzCardBorder, CircleShape)
                                .clickable {
                                    Toast.makeText(context, "تم عكس اتجاه التحويل", Toast.LENGTH_SHORT).show()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = "Swap",
                                tint = DzWhite,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // "إلى" (To: BTC)
                    Text(strings.to, color = DzTextSecondary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))

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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(DzGoldAccent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("₿", color = DzBlack, fontSize = 16.sp, fontWeight = FontWeight.Black)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("BTC (Bitcoin)", color = DzWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text("≈ $100.00 USD", color = DzTextSecondary, fontSize = 11.sp)
                                }
                            }

                            Text(
                                text = String.format("%.6f", calculatedBtc),
                                color = DzWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Exchange Rate details box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(DzCardBackground)
                            .border(1.dp, DzCardBorder, RoundedCornerShape(14.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(strings.exchangeRate, color = DzTextSecondary, fontSize = 12.sp)
                                Text("1 USDT = 0.000037 BTC", color = DzTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(strings.networkFee, color = DzTextSecondary, fontSize = 12.sp)
                                Text("1.00 USDT", color = DzTextPrimary, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Convert Pill Button
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                DzPillButton(
                    text = strings.convert,
                    isLoading = isLoading,
                    onClick = {
                        viewModel.exchangeCurrency(
                            fromAmount = fromAmount,
                            toBtcAmount = calculatedBtc,
                            onSuccess = {
                                Toast.makeText(context, "تم تحويل $fromAmount USDT إلى ${String.format("%.6f", calculatedBtc)} BTC بنجاح", Toast.LENGTH_LONG).show()
                                viewModel.navigateBack()
                            }
                        )
                    }
                )
            }
        }
    }
}
