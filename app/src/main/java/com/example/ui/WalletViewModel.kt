package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.NotificationItem
import com.example.data.model.TransactionItem
import com.example.data.model.UserProfile
import com.example.data.model.VirtualCardItem
import com.example.data.repository.WalletRepository
import com.example.ui.strings.LocaleManager
import com.example.ui.strings.Strings
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    SPLASH,
    AUTH,
    HOME,
    SEND,
    RECEIVE,
    BUY,
    CARDS,
    TRANSACTIONS,
    STORE,
    EXCHANGE,
    SECURITY,
    SUPPORT,
    NOTIFICATIONS,
    PROFILE,
    PARTNERS,
    PRIVACY_POLICY
}

class WalletViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WalletRepository(application.applicationContext)

    private val _currentLanguage = MutableStateFlow("ar")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    val strings: Strings
        get() = if (_currentLanguage.value == "ar") LocaleManager.Arabic else LocaleManager.French

    private val _currentScreen = MutableStateFlow(AppScreen.SPLASH)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val screenStack = mutableListOf<AppScreen>()

    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    private val _isBalanceExpanded = MutableStateFlow(true)
    val isBalanceExpanded: StateFlow<Boolean> = _isBalanceExpanded.asStateFlow()

    val userProfile: StateFlow<UserProfile?> = repository.observeProfile()
        .catch { emit(null) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = UserProfile()
        )

    val transactions: StateFlow<List<TransactionItem>> = repository.observeTransactions()
        .catch { emit(emptyList()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val cards: StateFlow<List<VirtualCardItem>> = repository.observeCards()
        .catch { emit(emptyList()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val notifications: StateFlow<List<NotificationItem>> = repository.observeNotifications()
        .catch { emit(emptyList()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun onUserAuthenticated(user: FirebaseUser) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.ensureInitialData(user)
                _currentScreen.value = AppScreen.HOME
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Failed to initialize user data"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            screenStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (screenStack.isNotEmpty()) {
            _currentScreen.value = screenStack.removeAt(screenStack.size - 1)
            return true
        }
        return false
    }

    fun setTab(index: Int) {
        _currentTab.value = index
        when (index) {
            0 -> _currentScreen.value = AppScreen.HOME
            1 -> _currentScreen.value = AppScreen.STORE
            2 -> _currentScreen.value = AppScreen.CARDS
            3 -> _currentScreen.value = AppScreen.PROFILE
        }
    }

    fun toggleBalanceExpanded() {
        _isBalanceExpanded.value = !_isBalanceExpanded.value
    }

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == "ar") "fr" else "ar"
    }

    fun sendUsdt(recipient: String, amount: Double, network: String, note: String, onSuccess: () -> Unit) {
        if (amount <= 0) {
            _errorMessage.value = strings.enterAmountError
            return
        }
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.sendUsdt(recipient, amount, network, note)
                _successMessage.value = strings.transactionSuccess
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Transfer failed"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun buyUsdt(amount: Double, paymentMethod: String, onSuccess: () -> Unit) {
        if (amount <= 0) {
            _errorMessage.value = strings.enterAmountError
            return
        }
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.buyUsdt(amount, paymentMethod)
                _successMessage.value = strings.transactionSuccess
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Purchase failed"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun exchangeCurrency(fromAmount: Double, toBtcAmount: Double, onSuccess: () -> Unit) {
        if (fromAmount <= 0) {
            _errorMessage.value = strings.enterAmountError
            return
        }
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.exchangeCurrency(fromAmount, toBtcAmount)
                _successMessage.value = strings.transactionSuccess
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Exchange failed"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleCardFreeze(card: VirtualCardItem) {
        viewModelScope.launch {
            try {
                repository.toggleCardFreeze(card)
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            }
        }
    }

    fun toggleOnlinePayment(card: VirtualCardItem) {
        viewModelScope.launch {
            try {
                repository.toggleOnlinePayment(card)
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            }
        }
    }

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }
}
