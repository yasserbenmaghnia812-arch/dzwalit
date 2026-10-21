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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.WalletViewModel
import com.example.ui.components.DzTopBar
import com.example.ui.theme.DzBlack
import com.example.ui.theme.DzCardBackground
import com.example.ui.theme.DzCardBorder
import com.example.ui.theme.DzCardElevated
import com.example.ui.theme.DzDarkBackground
import com.example.ui.theme.DzGreenBadge
import com.example.ui.theme.DzGreenIncome
import com.example.ui.theme.DzTextPrimary
import com.example.ui.theme.DzTextSecondary
import com.example.ui.theme.DzWhite

@Composable
fun SecurityScreen(viewModel: WalletViewModel) {
    BackHandler { viewModel.navigateBack() }

    val context = LocalContext.current
    val strings = viewModel.strings
    val profile by viewModel.userProfile.collectAsState()

    var showKycDialog by remember { mutableStateOf(false) }
    var showDevicesDialog by remember { mutableStateOf(false) }
    var showDataRightsDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DzDarkBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            item {
                DzTopBar(
                    title = strings.securityAndSettings,
                    onBackClick = { viewModel.navigateBack() }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Identity Verification Item
                SecurityMenuRow(
                    icon = Icons.Default.VerifiedUser,
                    title = strings.identityVerification,
                    trailing = {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(DzGreenBadge)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(strings.verified, color = DzGreenIncome, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    onClick = { showKycDialog = true }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // PIN Code
                SecurityMenuRow(
                    icon = Icons.Default.Pin,
                    title = strings.pinCode,
                    trailing = { Text("••••", color = DzTextSecondary, fontSize = 14.sp) },
                    onClick = { Toast.makeText(context, "رمز PIN محمي ومفعل", Toast.LENGTH_SHORT).show() }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Two-Factor Auth
                SecurityMenuRow(
                    icon = Icons.Default.Security,
                    title = strings.twoFactorAuth,
                    trailing = {
                        Text("TOTP مفعل", color = DzGreenIncome, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    },
                    onClick = { Toast.makeText(context, "المصادقة الثنائية 2FA مفعلة", Toast.LENGTH_SHORT).show() }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Device Management
                SecurityMenuRow(
                    icon = Icons.Default.Devices,
                    title = strings.deviceManagement,
                    trailing = { Text("جهازان نشطان", color = DzTextSecondary, fontSize = 12.sp) },
                    onClick = { showDevicesDialog = true }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Change Password
                SecurityMenuRow(
                    icon = Icons.Default.Lock,
                    title = strings.changePassword,
                    onClick = { Toast.makeText(context, "تم إرسال رابط تأمين وتغيير كلمة المرور إلى بريدك", Toast.LENGTH_SHORT).show() }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Algerian Law 07-18 Data Rights
                SecurityMenuRow(
                    icon = Icons.Default.PrivacyTip,
                    title = strings.privacySettings,
                    trailing = { Text("قانون 07-18", color = DzTextSecondary, fontSize = 11.sp) },
                    onClick = { showDataRightsDialog = true }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // KYC Dialog
        if (showKycDialog) {
            AlertDialog(
                onDismissRequest = { showKycDialog = false },
                containerColor = DzCardElevated,
                title = { Text("التحقق من الهوية (KYC / Liveness)", color = DzWhite, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("الحالة الحالية: مؤكد ومفعّل بالكامل ✓", color = DzGreenIncome, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("تم التحقق من بطاقة التعريف الوطنية وسيلفي التحقق الحيوي وفق ضوابط مكافحة غسل الأموال والقانون الجزائري.", color = DzTextSecondary, fontSize = 12.sp)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showKycDialog = false }) {
                        Text("حسناً", color = DzWhite)
                    }
                }
            )
        }

        // Devices Dialog
        if (showDevicesDialog) {
            AlertDialog(
                onDismissRequest = { showDevicesDialog = false },
                containerColor = DzCardElevated,
                title = { Text("الأجهزة النشطة والجلسات", color = DzWhite, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("1. هذا الجهاز (Android 15 - الجزائر)", color = DzWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text("آخر نشاط: الآن • IP: 197.200.0.1", color = DzGreenIncome, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("2. Chrome Web (Windows - الجزائر)", color = DzTextPrimary, fontSize = 13.sp)
                        Text("آخر نشاط: أمس 19:30", color = DzTextSecondary, fontSize = 11.sp)
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDevicesDialog = false
                        Toast.makeText(context, "تم إنهاء جميع الجلسات الأخرى بنجاح", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("إنهاء الجلسات الأخرى", color = DzWhite)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDevicesDialog = false }) {
                        Text("إلغاء", color = DzTextSecondary)
                    }
                }
            )
        }

        // Data Rights Dialog (Law 07-18)
        if (showDataRightsDialog) {
            AlertDialog(
                onDismissRequest = { showDataRightsDialog = false },
                containerColor = DzCardElevated,
                title = { Text("حقوق البيانات - القانون الجزائري 07-18", color = DzWhite, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("وفق أحكام القانون 07-18 المتعلق بحماية المعطيات ذات الطابع الشخصي:", color = DzTextPrimary, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• يحق لك طلب نسخة كاملة من بياناتك الشخصية المعالجة.", color = DzTextSecondary, fontSize = 12.sp)
                        Text("• يحق لك تصحيح أو تحديث بياناتك.", color = DzTextSecondary, fontSize = 12.sp)
                        Text("• يحق لك طلب حذف الحساب مع مراعاة الاحتفاظ بالسجلات المالية المفروضة قانونياً.", color = DzTextSecondary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("للتواصل مع مسؤول حماية البيانات: dzwalit@gmail.com", color = DzGreenIncome, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDataRightsDialog = false
                        viewModel.navigateTo(AppScreen.PRIVACY_POLICY)
                    }) {
                        Text("قراءة السياسة كاملة", color = DzWhite)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDataRightsDialog = false }) {
                        Text("إغلاق", color = DzTextSecondary)
                    }
                }
            )
        }
    }
}

@Composable
fun SecurityMenuRow(
    icon: ImageVector,
    title: String,
    trailing: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DzCardBackground)
            .border(1.dp, DzCardBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
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
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(DzCardElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = title, tint = DzWhite, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(14.dp))
                Text(text = title, color = DzTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }

            trailing?.invoke() ?: Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = DzTextSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
