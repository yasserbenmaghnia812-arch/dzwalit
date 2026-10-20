package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.ui.AppScreen
import com.example.ui.WalletViewModel
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.BuyUsdtScreen
import com.example.ui.screens.CardsScreen
import com.example.ui.screens.ExchangeScreen
import com.example.ui.screens.GlobalStoreScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.PartnerWelcomeScreen
import com.example.ui.screens.PrivacyPolicyScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ReceiveUsdtScreen
import com.example.ui.screens.SecurityScreen
import com.example.ui.screens.SendUsdtScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.SupportScreen
import com.example.ui.screens.TransactionsScreen
import com.example.ui.theme.DzDarkBackground
import com.example.ui.theme.DzWalletTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {

    private val viewModel: WalletViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DzWalletTheme(darkTheme = true) {
                DzWalletApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun DzWalletApp(viewModel: WalletViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()

    var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }
    val snackbarHostState = remember { SnackbarHostState() }

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
            if (auth.currentUser != null) {
                viewModel.onUserAuthenticated(auth.currentUser!!)
            }
        }
        Firebase.auth.addAuthStateListener(listener)
        onDispose {
            Firebase.auth.removeAuthStateListener(listener)
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(successMessage) {
        successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
    }

    // Direction RTL for Arabic, LTR for French
    val layoutDirection = if (currentLanguage == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Scaffold(
            containerColor = DzDarkBackground,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(DzDarkBackground)
            ) {
                when (currentScreen) {
                    AppScreen.SPLASH -> SplashScreen(viewModel = viewModel)
                    AppScreen.AUTH -> AuthScreen(viewModel = viewModel)
                    AppScreen.HOME -> HomeScreen(viewModel = viewModel)
                    AppScreen.SEND -> SendUsdtScreen(viewModel = viewModel)
                    AppScreen.RECEIVE -> ReceiveUsdtScreen(viewModel = viewModel)
                    AppScreen.BUY -> BuyUsdtScreen(viewModel = viewModel)
                    AppScreen.CARDS -> CardsScreen(viewModel = viewModel)
                    AppScreen.TRANSACTIONS -> TransactionsScreen(viewModel = viewModel)
                    AppScreen.STORE -> GlobalStoreScreen(viewModel = viewModel)
                    AppScreen.EXCHANGE -> ExchangeScreen(viewModel = viewModel)
                    AppScreen.SECURITY -> SecurityScreen(viewModel = viewModel)
                    AppScreen.SUPPORT -> SupportScreen(viewModel = viewModel)
                    AppScreen.NOTIFICATIONS -> NotificationsScreen(viewModel = viewModel)
                    AppScreen.PROFILE -> ProfileScreen(viewModel = viewModel)
                    AppScreen.PARTNERS -> PartnerWelcomeScreen(viewModel = viewModel)
                    AppScreen.PRIVACY_POLICY -> PrivacyPolicyScreen(viewModel = viewModel)
                }
            }
        }
    }
}
