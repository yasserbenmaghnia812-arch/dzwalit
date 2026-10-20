package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.WalletViewModel
import com.example.ui.components.DzTopBar
import com.example.ui.theme.DzBlack
import com.example.ui.theme.DzCardBackground
import com.example.ui.theme.DzCardBorder
import com.example.ui.theme.DzCardElevated
import com.example.ui.theme.DzDarkBackground
import com.example.ui.theme.DzGreenIncome
import com.example.ui.theme.DzTextMuted
import com.example.ui.theme.DzTextPrimary
import com.example.ui.theme.DzTextSecondary
import com.example.ui.theme.DzWhite

@Composable
fun PrivacyPolicyScreen(viewModel: WalletViewModel) {
    BackHandler { viewModel.navigateBack() }

    val context = LocalContext.current
    val isArabic = viewModel.currentLanguage.value == "ar"

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
                    title = if (isArabic) "سياسة الخصوصية" else "Politique de confidentialité",
                    onBackClick = { viewModel.navigateBack() }
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Legal Header Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DzCardBackground)
                        .border(1.dp, DzCardBorder, RoundedCornerShape(16.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Text(
                            text = "DZ Wallet — سياسة الخصوصية وحماية المعطيات",
                            color = DzWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "آخر تحديث: 20 أكتوبر 2026 | الإصدار: 1.0",
                            color = DzTextSecondary,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "وثيقة قانونية رسمية تحترم أحكام القانون الجزائري رقم 07-18 المتعلق بحماية الأشخاص الطبيعيين في مجال معالجة المعطيات ذات الطابع الشخصي.",
                            color = DzGreenIncome,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Section 1: Data Collection
                PolicySectionCard(
                    title = "1. البيانات التي نجمعها",
                    content = "1.1 بيانات الحساب: الاسم الكامل، البريد الإلكتروني، رقم الهاتف، وكلمة المرور (مخزنة مشفرة ومهشمة فقط).\n\n" +
                            "1.2 التحقق من الهوية (KYC/AML): عند طلب الخدمات المالية، نجمع صورة وثيقة هوية رسمية وسيلفي التحقق الحيوي (Liveness) مع معالجة آمنة مشفرة.\n\n" +
                            "1.3 بيانات المعاملات: عناوين البلوك تشين، معرفات العمليات TxID، المبالغ والرسوم وحالات التأكيد.\n\n" +
                            "1.4 بيانات البطاقات: نوع البطاقة وحالتها وآخر 4 أرقام فقط — لا نقوم بتخزين رقم البطاقة الكامل أو رمز CVV إطلاقاً."
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Section 2: Non-Custodial Core Principle
                PolicySectionCard(
                    title = "2. مبدأ الأمان الأساسي والمفاتيح الخاصة",
                    content = "• لا نخزن المفاتيح الخاصة أو عبارات الاسترداد على خوادمنا في وضع الحفظ الذاتي، وتظل مشفرة على جهازك فقط عبر Android Keystore.\n\n" +
                            "• عناوين المحافظ والمعاملات على شبكات البلوك تشين عامة بطبيعتها، ولا يمكن تعديلها أو حذفها من السجل الموزع."
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Section 3: Law 07-18 User Rights
                PolicySectionCard(
                    title = "3. حقوق المستخدم بموجب القانون الجزائري 07-18",
                    content = "بموجب القانون 07-18، يتمتع المستخدم بالحقوق التالية:\n" +
                            "• الحق في الاطلاع على المعطيات الشخصية والحصول على نسخة منها.\n" +
                            "• الحق في تصحيح أو تحيين أي معطيات غير دقيقة.\n" +
                            "• الحق في طلب الحذف والنسيان، مع مراعاة القوانين المالية التي تفرض حفظ سجل المعاملات المالية لمدد محددة قانونياً.\n" +
                            "• ممارسة الحقوق مجانية عبر البريد الإلكتروني المخصص: privacy@dzwallet.app"
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Section 4: Minors & 18+ Rule
                PolicySectionCard(
                    title = "4. حماية القاصرين (+18 سنة)",
                    content = "تطبيق DZ Wallet موجه حصرياً للمستخدمين البالغين (18 سنة فأكثر). لا نقوم بجمع أو معالجة معطيات القاصرين عمداً، ويتم فوراً حذف أي حساب يثبت أنه يعود لشخص دون السن القانوني."
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Section 5: Web Reference & Contact
                PolicySectionCard(
                    title = "5. الرابط المرجعي وقنوات التواصل",
                    content = "الرابط الرسمي لسياسة الخصوصية:\nhttps://www.dzwallet.app/privacy-policy\n\n" +
                            "مسؤول معالجة وحماية المعطيات:\nالبريد: privacy@dzwallet.app\n" +
                            "العنوان: الجزائر العاصمة، الجمهورية الجزائرية الديمقراطية الشعبية."
                )

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}

@Composable
fun PolicySectionCard(title: String, content: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DzCardBackground)
            .border(1.dp, DzCardBorder, RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = title,
                color = DzWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                color = DzTextSecondary,
                fontSize = 12.sp,
                lineHeight = 19.sp
            )
        }
    }
}
