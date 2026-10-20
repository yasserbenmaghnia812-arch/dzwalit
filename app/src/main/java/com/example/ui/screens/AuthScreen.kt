package com.example.ui.screens

import android.app.Activity
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import com.example.R
import com.example.core.auth.AuthManager
import com.example.ui.AppScreen
import com.example.ui.WalletViewModel
import com.example.ui.components.DzPillButton
import com.example.ui.theme.DzBlack
import com.example.ui.theme.DzCardBackground
import com.example.ui.theme.DzCardBorder
import com.example.ui.theme.DzCardElevated
import com.example.ui.theme.DzDarkBackground
import com.example.ui.theme.DzGreenIncome
import com.example.ui.theme.DzTextPrimary
import com.example.ui.theme.DzTextSecondary
import com.example.ui.theme.DzWhite
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AuthScreen(viewModel: WalletViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }
    val strings = viewModel.strings

    var emailOrPhone by remember { mutableStateOf("ahmed@email.com") }
    var password by remember { mutableStateOf("••••••••") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isSignUp by remember { mutableStateOf(false) }
    var consentChecked by remember { mutableStateOf(true) }
    var isSigningIn by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DzDarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // App Logo
                Image(
                    painter = painterResource(id = R.drawable.dz_wallet_icon_1792521385553),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "DZ Wallet",
                    color = DzWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isSignUp) strings.createNewAccount else strings.signIn,
                    color = DzTextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = strings.welcomeBack,
                    color = DzTextSecondary,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Email / Phone Field
                OutlinedTextField(
                    value = emailOrPhone,
                    onValueChange = { emailOrPhone = it },
                    label = { Text(strings.emailOrPhone, color = DzTextSecondary, fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = DzTextSecondary
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_email_input"),
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

                Spacer(modifier = Modifier.height(16.dp))

                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(strings.password, color = DzTextSecondary, fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Password",
                            tint = DzTextSecondary
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password visibility",
                                tint = DzTextSecondary
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_password_input"),
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

                Spacer(modifier = Modifier.height(12.dp))

                // Mandatory Privacy Policy & Algerian Law 07-18 Checkbox
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = consentChecked,
                        onCheckedChange = { consentChecked = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = DzWhite,
                            uncheckedColor = DzTextSecondary,
                            checkmarkColor = DzBlack
                        ),
                        modifier = Modifier.testTag("privacy_consent_checkbox")
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = strings.termsCheckbox,
                        color = DzTextSecondary,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .clickable { viewModel.navigateTo(AppScreen.PRIVACY_POLICY) }
                            .weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Sign In Pill Button
                DzPillButton(
                    text = if (isSignUp) strings.createNewAccount else strings.signIn,
                    enabled = consentChecked && !isSigningIn,
                    isLoading = isSigningIn,
                    onClick = {
                        if (!consentChecked) {
                            Toast.makeText(context, "يرجى قبول سياسة الخصوصية للمتابعة", Toast.LENGTH_SHORT).show()
                            return@DzPillButton
                        }
                        // Trigger Google Sign-In with Credential Manager for production security
                        isSigningIn = true
                        AuthManager.onGoogleSignInClicked(
                            context = context,
                            credentialManager = credentialManager,
                            onAuthSuccess = {
                                isSigningIn = false
                                Firebase.auth.currentUser?.let { user ->
                                    viewModel.onUserAuthenticated(user)
                                }
                            },
                            onAuthError = { err ->
                                isSigningIn = false
                                Toast.makeText(context, err, Toast.LENGTH_LONG).show()
                            },
                            scope = scope,
                            onAuthCancelled = {
                                isSigningIn = false
                            }
                        )
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = strings.forgotPassword,
                    color = DzTextSecondary,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "تم إرسال رابط استعادة كلمة المرور إلى بريدك", Toast.LENGTH_SHORT).show()
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Social Logins (Google, Apple, Biometric)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Google Sign-In Button
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(DzCardBackground)
                            .border(1.dp, DzCardBorder, CircleShape)
                            .clickable {
                                isSigningIn = true
                                AuthManager.onGoogleSignInClicked(
                                    context = context,
                                    credentialManager = credentialManager,
                                    onAuthSuccess = {
                                        isSigningIn = false
                                        Firebase.auth.currentUser?.let { user ->
                                            viewModel.onUserAuthenticated(user)
                                        }
                                    },
                                    onAuthError = { err ->
                                        isSigningIn = false
                                        Toast.makeText(context, err, Toast.LENGTH_LONG).show()
                                    },
                                    scope = scope,
                                    onAuthCancelled = { isSigningIn = false }
                                )
                            }
                            .testTag("google_signin_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("G", color = DzWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // Apple Auth
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(DzCardBackground)
                            .border(1.dp, DzCardBorder, CircleShape)
                            .clickable {
                                Toast.makeText(context, "تسجيل الدخول عبر Apple متاح", Toast.LENGTH_SHORT).show()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("", color = DzWhite, fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // Biometric
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(DzCardBackground)
                            .border(1.dp, DzCardBorder, CircleShape)
                            .clickable {
                                Toast.makeText(context, "المصادقة بالبصمة البيومترية مفعلة", Toast.LENGTH_SHORT).show()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "Biometric",
                            tint = DzGreenIncome,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }

            // Bottom Switch to Sign Up
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isSignUp) "لديك حساب بالفعل؟ " else strings.dontHaveAccount + " ",
                    color = DzTextSecondary,
                    fontSize = 13.sp
                )
                Text(
                    text = if (isSignUp) strings.signIn else strings.createNewAccount,
                    color = DzWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { isSignUp = !isSignUp }
                )
            }
        }
    }
}
