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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import com.example.core.auth.AuthManager
import com.example.ui.AppScreen
import com.example.ui.WalletViewModel
import com.example.ui.components.DzBottomNavBar
import com.example.ui.components.DzTopBar
import com.example.ui.theme.DzBlack
import com.example.ui.theme.DzCardBackground
import com.example.ui.theme.DzCardBorder
import com.example.ui.theme.DzCardElevated
import com.example.ui.theme.DzDarkBackground
import com.example.ui.theme.DzRedExpense
import com.example.ui.theme.DzTextPrimary
import com.example.ui.theme.DzTextSecondary
import com.example.ui.theme.DzWhite
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun ProfileScreen(viewModel: WalletViewModel) {
    BackHandler { viewModel.setTab(0) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val strings = viewModel.strings
    val currentTab by viewModel.currentTab.collectAsState()
    val profile by viewModel.userProfile.collectAsState()
    val credentialManager = androidx.compose.runtime.remember { CredentialManager.create(context) }

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
                    title = strings.more,
                    onBackClick = { viewModel.setTab(0) }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Profile Avatar & User Details
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DzCardBackground)
                        .border(1.dp, DzCardBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(DzCardElevated)
                            .border(1.dp, DzCardBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (profile?.displayName?.firstOrNull() ?: 'A').uppercase(),
                            color = DzWhite,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = profile?.displayName ?: "Ahmed Benali",
                            color = DzWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = profile?.email ?: "dzwalit@gmail.com",
                            color = DzTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Menu items matching screenshot screen 14
                ProfileMenuItem(
                    icon = Icons.Default.CreditCard,
                    title = strings.cards,
                    onClick = { viewModel.navigateTo(AppScreen.CARDS) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                ProfileMenuItem(
                    icon = Icons.Default.Wallet,
                    title = strings.wallet,
                    onClick = { viewModel.setTab(0) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                ProfileMenuItem(
                    icon = Icons.Default.EmojiEvents,
                    title = strings.myRewards,
                    onClick = {
                        Toast.makeText(context, "لديك 250 نقطة مكافآت صالحة للاستبدال بـ USDT", Toast.LENGTH_SHORT).show()
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                ProfileMenuItem(
                    icon = Icons.Default.History,
                    title = strings.activityLog,
                    onClick = { viewModel.navigateTo(AppScreen.TRANSACTIONS) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                ProfileMenuItem(
                    icon = Icons.Default.Settings,
                    title = strings.settings,
                    onClick = { viewModel.navigateTo(AppScreen.SECURITY) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                ProfileMenuItem(
                    icon = Icons.Default.PrivacyTip,
                    title = strings.privacyPolicy,
                    onClick = { viewModel.navigateTo(AppScreen.PRIVACY_POLICY) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                ProfileMenuItem(
                    icon = Icons.Default.Language,
                    title = strings.switchLanguage,
                    onClick = { viewModel.toggleLanguage() }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Sign Out
                ProfileMenuItem(
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    title = strings.signOut,
                    textColor = DzRedExpense,
                    onClick = {
                        AuthManager.signOut(
                            context = context,
                            credentialManager = credentialManager,
                            onSignOutComplete = {
                                viewModel.navigateTo(AppScreen.AUTH)
                            },
                            scope = scope
                        )
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    textColor: Color = DzTextPrimary,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DzCardBackground)
            .border(1.dp, DzCardBorder, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
            .testTag("menu_item_${title.take(8)}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = title, tint = textColor, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(14.dp))
                Text(text = title, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = DzTextSecondary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
