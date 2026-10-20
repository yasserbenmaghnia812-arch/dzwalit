package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.AppScreen
import com.example.ui.WalletViewModel
import com.example.ui.components.DzDottedGlobe
import com.example.ui.components.DzPillButton
import com.example.ui.theme.DzBlack
import com.example.ui.theme.DzCardBackground
import com.example.ui.theme.DzCardBorder
import com.example.ui.theme.DzDarkBackground
import com.example.ui.theme.DzTextPrimary
import com.example.ui.theme.DzTextSecondary
import com.example.ui.theme.DzWhite

@Composable
fun SplashScreen(viewModel: WalletViewModel) {
    val strings = viewModel.strings

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DzDarkBackground)
            .padding(24.dp)
    ) {
        // Language switcher top-right
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(DzCardBackground)
                    .border(1.dp, DzCardBorder, RoundedCornerShape(16.dp))
                    .clickable { viewModel.toggleLanguage() }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
                    .testTag("lang_toggle_splash")
            ) {
                Text(
                    text = viewModel.strings.switchLanguage,
                    color = DzTextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Center Content: Logo, Dotted Globe, Taglines
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, DzCardBorder, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dz_wallet_icon_1792521385553),
                    contentDescription = "DZ Wallet Logo",
                    modifier = Modifier.size(80.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "DZ Wallet",
                color = DzWhite,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = strings.tagline,
                color = DzTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = strings.subtitle,
                color = DzTextSecondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Dotted Globe Visualization
            DzDottedGlobe(modifier = Modifier.size(180.dp))
        }

        // Bottom Actions: "ابدأ الآن" + "لدي حساب بالفعل"
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DzPillButton(
                text = strings.getStarted,
                onClick = { viewModel.navigateTo(AppScreen.PARTNERS) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = { viewModel.navigateTo(AppScreen.AUTH) },
                modifier = Modifier.testTag("already_have_account_button")
            ) {
                Text(
                    text = strings.alreadyHaveAccount,
                    color = DzTextSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
