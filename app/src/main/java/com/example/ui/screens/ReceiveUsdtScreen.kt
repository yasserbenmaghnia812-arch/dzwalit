package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.WalletViewModel
import com.example.ui.components.DzPillButton
import com.example.ui.components.DzQrCodeView
import com.example.ui.components.DzTopBar
import com.example.ui.theme.DzBlack
import com.example.ui.theme.DzCardBackground
import com.example.ui.theme.DzCardBorder
import com.example.ui.theme.DzDarkBackground
import com.example.ui.theme.DzGreenIncome
import com.example.ui.theme.DzTextPrimary
import com.example.ui.theme.DzTextSecondary
import com.example.ui.theme.DzWhite

@Composable
fun ReceiveUsdtScreen(viewModel: WalletViewModel) {
    BackHandler { viewModel.navigateBack() }

    val context = LocalContext.current
    val strings = viewModel.strings
    val walletAddress = "TQ8v7k9sWimf42e3tfkJALts39m7"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DzDarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                DzTopBar(
                    title = strings.receiveUsdt,
                    onBackClick = { viewModel.navigateBack() }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Network Badge: USDT (TRC20)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(DzCardBackground)
                        .border(1.dp, DzCardBorder, RoundedCornerShape(16.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(DzGreenIncome),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("₮", color = DzBlack, fontSize = 12.sp, fontWeight = FontWeight.Black)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "USDT (TRC20)",
                        color = DzWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Big QR Code
                DzQrCodeView(
                    data = walletAddress,
                    size = 230.dp,
                    modifier = Modifier.testTag("receive_qr_code")
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = strings.scanQrNotice,
                    color = DzTextSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Wallet Address Container
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text(
                        text = strings.walletAddress,
                        color = DzTextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(DzCardBackground)
                            .border(1.dp, DzCardBorder, RoundedCornerShape(14.dp))
                            .clickable {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("DZ Wallet Address", walletAddress)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, strings.copySuccess, Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                            .testTag("copy_address_container"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = walletAddress,
                            color = DzTextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = DzTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Share Address Pill Button
            Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                DzPillButton(
                    text = strings.shareAddress,
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "عنوان محفظة USDT (TRC20) الخاصة بي في DZ Wallet:\n$walletAddress")
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    }
                )
            }
        }
    }
}
