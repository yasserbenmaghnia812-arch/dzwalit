package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ContactSupport
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.WalletViewModel
import com.example.ui.components.DzPillButton
import com.example.ui.components.DzTopBar
import com.example.ui.theme.DzBlack
import com.example.ui.theme.DzCardBackground
import com.example.ui.theme.DzCardBorder
import com.example.ui.theme.DzCardElevated
import com.example.ui.theme.DzDarkBackground
import com.example.ui.theme.DzTextPrimary
import com.example.ui.theme.DzTextSecondary
import com.example.ui.theme.DzWhite

@Composable
fun SupportScreen(viewModel: WalletViewModel) {
    BackHandler { viewModel.navigateBack() }

    val context = LocalContext.current
    val strings = viewModel.strings

    var faqOpenIndex by remember { mutableStateOf<Int?>(null) }

    val faqItems = listOf(
        Pair("ما هي شبكات USDT المدعومة في التطبيق؟", "ندعم شبكة Tron (TRC-20) كشبكة أساسية منخفضة الرسوم (1 USDT)، مع دعم اختياري لشبكة Ethereum (ERC-20)."),
        Pair("كيف يتم تفعيل وإصدار بطاقة VISA الافتراضية؟", "تُصدر البطاقة فور استيفاء التحقق من الهوية (KYC) وتتيح الدفع المباشر عبر الإنترنت مع ربطها برصيد محفظتك."),
        Pair("هل تطبقون القانون الجزائري 07-18 لحماية المعطيات؟", "نعم، نلتزم تماماً بالقانون 07-18 ولا نشارك بياناتك إطلاقاً مع أطراف غير مصرح لها، مع حقك الكامل في طلب الاطلاع أو الحذف عبر privacy@dzwallet.app."),
        Pair("ماذا تفعل عند انقطاع الاتصال أثناء المعاملة؟", "يتحقق النظام أولاً من المعاملة عبر TxID على البلوك تشين لمنع أي ازدواجية أو خصم مضاعف للأموال.")
    )

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
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
            ) {
                item {
                    DzTopBar(
                        title = strings.helpAndSupport,
                        onBackClick = { viewModel.navigateBack() }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = strings.faq,
                        color = DzWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(faqItems.size) { index ->
                    val (question, answer) = faqItems[index]
                    val isOpen = faqOpenIndex == index
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(DzCardBackground)
                            .border(1.dp, DzCardBorder, RoundedCornerShape(14.dp))
                            .clickable { faqOpenIndex = if (isOpen) null else index }
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = question,
                                    color = DzTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = if (isOpen) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = DzTextSecondary
                                )
                            }
                            AnimatedVisibility(visible = isOpen) {
                                Column {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = answer,
                                        color = DzTextSecondary,
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "قنوات التواصل",
                        color = DzWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Contact row
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(DzCardBackground)
                            .border(1.dp, DzCardBorder, RoundedCornerShape(14.dp))
                            .clickable {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:privacy@dzwallet.app")
                                    putExtra(Intent.EXTRA_SUBJECT, "DZ Wallet Support Request")
                                }
                                context.startActivity(intent)
                            }
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(DzCardElevated),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Email, contentDescription = null, tint = DzWhite, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("البريد الإلكتروني للدعم والخصوصية", color = DzTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("privacy@dzwallet.app", color = DzTextSecondary, fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // Live Chat Pill Button
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                DzPillButton(
                    text = strings.liveChat,
                    onClick = {
                        Toast.makeText(context, "جاري فتح تذكرة دعم فوري مع مهندس الدعم المالي", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}
