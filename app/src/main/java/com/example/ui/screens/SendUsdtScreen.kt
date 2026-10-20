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
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.ui.theme.DzGreenIncome
import com.example.ui.theme.DzRedExpense
import com.example.ui.theme.DzTextPrimary
import com.example.ui.theme.DzTextSecondary
import com.example.ui.theme.DzWhite

@Composable
fun SendUsdtScreen(viewModel: WalletViewModel) {
    BackHandler { viewModel.navigateBack() }

    val context = LocalContext.current
    val strings = viewModel.strings
    val profile by viewModel.userProfile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var recipient by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("100") }
    var selectedNetwork by remember { mutableStateOf("USDT (TRC20)") }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val amountValue = amountText.toDoubleOrNull() ?: 0.0
    val networkFee = 1.00
    val totalAmount = if (amountValue > 0) amountValue + networkFee else 0.0

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
                    title = strings.sendUsdt,
                    onBackClick = { viewModel.navigateBack() }
                )

                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // "إلى" (Recipient input with QR scan button)
                    Text(
                        text = strings.toRecipient,
                        color = DzTextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = recipient,
                        onValueChange = { recipient = it },
                        placeholder = { Text(strings.searchRecipientHint, color = DzTextSecondary, fontSize = 12.sp) },
                        trailingIcon = {
                            IconButton(onClick = {
                                recipient = "TQ8v7k9sWimf42e3tfkJALts39m7"
                                Toast.makeText(context, "تم مسح العنوان بنجاح", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.QrCodeScanner,
                                    contentDescription = "Scan QR",
                                    tint = DzTextPrimary
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("send_recipient_input"),
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

                    Spacer(modifier = Modifier.height(20.dp))

                    // Currency / Network Selection
                    Text(
                        text = strings.currency,
                        color = DzTextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(DzCardBackground)
                            .border(1.dp, DzCardBorder, RoundedCornerShape(14.dp))
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(DzGreenIncome),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("₮", color = DzBlack, fontSize = 14.sp, fontWeight = FontWeight.Black)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = selectedNetwork,
                                color = DzTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "TRC20 ▾",
                            color = DzTextSecondary,
                            fontSize = 13.sp,
                            modifier = Modifier.clickable {
                                selectedNetwork = if (selectedNetwork.contains("TRC20")) "USDT (ERC20)" else "USDT (TRC20)"
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Amount input
                    Text(
                        text = strings.amount,
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
                            .testTag("send_amount_input"),
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
                        text = "= $${String.format("%.2f", amountValue)}",
                        color = DzTextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Fee & Total Box
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
                                Text(strings.networkFee, color = DzTextSecondary, fontSize = 13.sp)
                                Text("$networkFee USDT", color = DzTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(strings.total, color = DzWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    "${String.format("%.2f", totalAmount)} USDT",
                                    color = DzWhite,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Continue Action
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                DzPillButton(
                    text = strings.continueAction,
                    isLoading = isLoading,
                    onClick = {
                        if (recipient.isBlank()) {
                            Toast.makeText(context, "يرجى تحديد عنوان المستلم", Toast.LENGTH_SHORT).show()
                            return@DzPillButton
                        }
                        if (amountValue <= 0) {
                            Toast.makeText(context, strings.enterAmountError, Toast.LENGTH_SHORT).show()
                            return@DzPillButton
                        }
                        val userBalance = profile?.usdtBalance ?: 1245.32
                        if (totalAmount > userBalance) {
                            Toast.makeText(context, "الرصيد غير كافٍ لتغطية المبلغ والرسوم", Toast.LENGTH_SHORT).show()
                            return@DzPillButton
                        }
                        showConfirmDialog = true
                    }
                )
            }
        }

        // Confirmation Dialog
        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                containerColor = DzCardElevated,
                title = {
                    Text("تأكيد إرسال USDT", color = DzWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                },
                text = {
                    Column {
                        Text("أنت على وشك إرسال:", color = DzTextSecondary, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "$amountValue USDT + $networkFee رسوم",
                            color = DzGreenIncome,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("إلى: ${recipient.take(12)}...${recipient.takeLast(6)}", color = DzTextPrimary, fontSize = 12.sp)
                        Text("الشبكة: $selectedNetwork", color = DzTextSecondary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("✓ مصادقة أمنية مشفرة", color = DzTextSecondary, fontSize = 11.sp)
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showConfirmDialog = false
                            viewModel.sendUsdt(
                                recipient = recipient,
                                amount = amountValue,
                                network = if (selectedNetwork.contains("TRC20")) "TRC20" else "ERC20",
                                note = "إرسال إلى المستلم",
                                onSuccess = {
                                    Toast.makeText(context, strings.transactionSuccess, Toast.LENGTH_LONG).show()
                                    viewModel.navigateBack()
                                }
                            )
                        }
                    ) {
                        Text("تأكيد والإرسال", color = DzWhite, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmDialog = false }) {
                        Text("إلغاء", color = DzTextSecondary)
                    }
                }
            )
        }
    }
}
