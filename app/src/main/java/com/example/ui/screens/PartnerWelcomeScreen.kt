package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.AppScreen
import com.example.ui.WalletViewModel
import com.example.ui.components.DzPillButton
import com.example.ui.components.DzVirtualCard
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
fun PartnerWelcomeScreen(viewModel: WalletViewModel) {
    BackHandler { viewModel.navigateBack() }

    val strings = viewModel.strings

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DzDarkBackground)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(20.dp))

                // DZ Logo
                Image(
                    painter = painterResource(id = R.drawable.dz_wallet_icon_1792521385553),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(14.dp))
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "DZ Wallet",
                    color = DzWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "استخدم بطاقتك في أي مكان في العالم",
                    color = DzWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "تسوق. ادفع. استلم. بكل سهولة وأمان.",
                    color = DzTextSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Virtual Card Display
                DzVirtualCard(
                    cardHolder = "AHMED BENALI",
                    last4 = "4821",
                    status = "active"
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Partner Brand Logos (Amazon, eBay, Walmart, Apple, Google, TRON)
                Text(
                    text = "شركاء الدفع والتجارة العالمية",
                    color = DzTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PartnerChip("amazon", Color(0xFFFF9900))
                    PartnerChip("ebay", Color(0xFF0064D2))
                    PartnerChip("Walmart", Color(0xFF0071DC))
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PartnerChip(" Apple Pay", DzWhite)
                    PartnerChip("Google Pay", Color(0xFF4285F4))
                    PartnerChip("TRON Network", DzGreenIncome)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Start Now button
            Column(modifier = Modifier.fillMaxWidth()) {
                DzPillButton(
                    text = strings.getStarted,
                    onClick = { viewModel.navigateTo(AppScreen.AUTH) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun PartnerChip(name: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DzCardBackground)
            .border(1.dp, DzCardBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = name,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
