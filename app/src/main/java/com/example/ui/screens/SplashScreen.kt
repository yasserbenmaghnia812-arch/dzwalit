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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
import com.example.ui.theme.DzBlack
import com.example.ui.theme.DzCardBackground
import com.example.ui.theme.DzCardBorder
import com.example.ui.theme.DzDarkBackground
import com.example.ui.theme.DzGreenIncome
import com.example.ui.theme.DzTextPrimary
import com.example.ui.theme.DzTextSecondary
import com.example.ui.theme.DzWhite

@Composable
fun SplashScreen(viewModel: WalletViewModel) {
    val currentLang = viewModel.currentLanguage.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF040A07),
                        Color(0xFF07140E),
                        Color(0xFF06100B),
                        Color(0xFF030705)
                    )
                )
            )
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // Language switcher at top corner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0B1F16))
                    .border(1.dp, Color(0xFF143B2B), RoundedCornerShape(12.dp))
                    .clickable { viewModel.toggleLanguage() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("splash_lang_toggle")
            ) {
                Text(
                    text = if (currentLang == "ar") "FR" else "عربي",
                    color = DzWhite,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(28.dp))

                // DZ Wallet Logo (white stylized geometric icon)
                Image(
                    painter = painterResource(id = R.drawable.dz_wallet_icon_1792521385553),
                    contentDescription = "DZ Wallet Logo",
                    modifier = Modifier
                        .size(58.dp)
                        .clip(RoundedCornerShape(14.dp))
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "DZ Wallet",
                    color = DzWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 3D Holographic Globe
                Box(
                    modifier = Modifier
                        .size(230.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.dz_globe_3d),
                        contentDescription = "Holographic Globe",
                        modifier = Modifier.size(230.dp)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // 3 Feature items with glowing rounded square icons (as in the screenshot)
                FeatureGlowRow(
                    icon = Icons.Default.CreditCard,
                    title = if (currentLang == "ar") "مدفوعات عالمية" else "Paiements mondiaux"
                )

                Spacer(modifier = Modifier.height(18.dp))

                FeatureGlowRow(
                    icon = Icons.Default.SwapHoriz,
                    title = if (currentLang == "ar") "تحويل سريع" else "Transfert rapide"
                )

                Spacer(modifier = Modifier.height(18.dp))

                FeatureGlowRow(
                    icon = Icons.Default.Lock,
                    title = if (currentLang == "ar") "أمان متقدم" else "Sécurité avancée"
                )

                Spacer(modifier = Modifier.height(32.dp))
            }

            // Bottom Buttons: Emerald Pill "ابدأ الآن" + Subtitle "لديك حساب بالفعل؟ سجل الدخول"
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF0F6D44),
                                    Color(0xFF138A56),
                                    Color(0xFF0E633E)
                                )
                            )
                        )
                        .clickable { viewModel.navigateTo(AppScreen.AUTH) }
                        .testTag("splash_start_now_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (currentLang == "ar") "ابدأ الآن" else "Commencer",
                        color = DzWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                TextButton(
                    onClick = { viewModel.navigateTo(AppScreen.AUTH) },
                    modifier = Modifier.testTag("splash_sign_in_text_btn")
                ) {
                    Text(
                        text = if (currentLang == "ar") "لديك حساب بالفعل؟ سجل الدخول" else "Vous avez déjà un compte ? Connexion",
                        color = Color(0xFF8FA79A),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun FeatureGlowRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Glowing rounded square icon container
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0A2318))
                .border(1.5.dp, Color(0xFF1B593E), RoundedCornerShape(12.dp))
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp), spotColor = Color(0xFF10B981)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF4ADE80),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(18.dp))

        Text(
            text = title,
            color = DzWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
