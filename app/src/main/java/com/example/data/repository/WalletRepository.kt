package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.core.util.OperationType
import com.example.core.util.handleFirestoreError
import com.example.data.model.ConsentRecordItem
import com.example.data.model.NotificationItem
import com.example.data.model.TransactionItem
import com.example.data.model.UserProfile
import com.example.data.model.VirtualCardItem
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID

class WalletRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing DZ Wallet.")
    }

    fun observeProfile(): Flow<UserProfile?> = flow {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid)
        emitAll(
            docRef.snapshots()
                .map { snapshot -> snapshot.toObject(UserProfile::class.java) }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.GET, docRef.path)
                    throw error
                }
        )
    }

    suspend fun ensureInitialData(user: FirebaseUser) {
        val uid = user.uid
        val userRef = db.collection("users").document(uid)
        val snapshot = userRef.get().await()
        if (!snapshot.exists()) {
            val profile = hashMapOf(
                "userId" to uid,
                "displayName" to (user.displayName ?: "مستخدم DZ Wallet"),
                "email" to (user.email ?: "user@dzwallet.app"),
                "phoneNumber" to (user.phoneNumber ?: "+213 555 12 34 56"),
                "kycStatus" to "verified",
                "biometricEnabled" to true,
                "twoFactorEnabled" to true,
                "pinEnabled" to true,
                "pinCode" to "1234",
                "privacyPolicyAccepted" to true,
                "language" to "ar",
                "usdtBalance" to 1245.32,
                "btcBalance" to 0.046,
                "dzdBalance" to 311330.0,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            userRef.set(profile).await()

            // Seed initial transactions matching reference screenshot
            val txCol = userRef.collection("transactions")
            val defaultTxs = listOf(
                hashMapOf(
                    "userId" to uid,
                    "type" to "receive",
                    "amount" to 250.00,
                    "currency" to "USDT",
                    "network" to "TRC20",
                    "recipient" to "محفظتي",
                    "sender" to "Binance",
                    "txId" to "0x7a89b...3f9c",
                    "status" to "confirmed",
                    "fee" to 1.0,
                    "counterpartAmount" to 250.0,
                    "counterpartCurrency" to "USD",
                    "note" to "استلام من Binance",
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                ),
                hashMapOf(
                    "userId" to uid,
                    "type" to "send",
                    "amount" to 50.00,
                    "currency" to "USDT",
                    "network" to "TRC20",
                    "recipient" to "علي",
                    "sender" to "محفظتي",
                    "txId" to "0x4b12c...88a1",
                    "status" to "confirmed",
                    "fee" to 1.0,
                    "counterpartAmount" to 50.0,
                    "counterpartCurrency" to "USD",
                    "note" to "دفع إلى علي",
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                ),
                hashMapOf(
                    "userId" to uid,
                    "type" to "card_payment",
                    "amount" to 120.00,
                    "currency" to "USDT",
                    "network" to "VISA",
                    "recipient" to "Amazon",
                    "sender" to "بطاقة VISA",
                    "txId" to "VISA-99120",
                    "status" to "confirmed",
                    "fee" to 0.0,
                    "counterpartAmount" to 120.0,
                    "counterpartCurrency" to "USD",
                    "note" to "شراء من Amazon",
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                ),
                hashMapOf(
                    "userId" to uid,
                    "type" to "receive",
                    "amount" to 75.00,
                    "currency" to "USDT",
                    "network" to "TRC20",
                    "recipient" to "محفظتي",
                    "sender" to "صديق",
                    "txId" to "0x98cf2...11d4",
                    "status" to "confirmed",
                    "fee" to 1.0,
                    "counterpartAmount" to 75.0,
                    "counterpartCurrency" to "USD",
                    "note" to "إرسال إلى صديق",
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            )
            for (tx in defaultTxs) {
                txCol.add(tx).await()
            }

            // Seed initial Virtual Card
            val cardCol = userRef.collection("cards")
            val defaultCard = hashMapOf(
                "userId" to uid,
                "cardHolder" to (user.displayName?.uppercase() ?: "AHMED BENALI"),
                "last4" to "4821",
                "cardType" to "Virtual Card - VISA",
                "status" to "active",
                "spendingLimit" to 5000.0,
                "availableBalance" to 1245.32,
                "onlinePaymentEnabled" to true,
                "contactlessEnabled" to true,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            cardCol.add(defaultCard).await()

            // Seed initial Notifications
            val notifCol = userRef.collection("notifications")
            val defaultNotifs = listOf(
                hashMapOf(
                    "userId" to uid,
                    "title" to "تم استلام 250 USDT",
                    "body" to "من حساب Binance بنجاح إلى محفظتك",
                    "type" to "transfer",
                    "read" to false,
                    "createdAt" to FieldValue.serverTimestamp()
                ),
                hashMapOf(
                    "userId" to uid,
                    "title" to "تم تأكيد عملية الشراء",
                    "body" to "دفع ببطاقة VISA لدى متجر Amazon",
                    "type" to "order",
                    "read" to true,
                    "createdAt" to FieldValue.serverTimestamp()
                ),
                hashMapOf(
                    "userId" to uid,
                    "title" to "تحديث أمان الحساب",
                    "body" to "تم تفعيل المصادقة الثنائية بنجاح",
                    "type" to "security",
                    "read" to true,
                    "createdAt" to FieldValue.serverTimestamp()
                )
            )
            for (notif in defaultNotifs) {
                notifCol.add(notif).await()
            }

            // Record initial consent
            val consentCol = userRef.collection("consent_records")
            consentCol.add(
                hashMapOf(
                    "userId" to uid,
                    "policyVersion" to "1.0",
                    "termsAccepted" to true,
                    "acceptedAt" to FieldValue.serverTimestamp(),
                    "ipAddress" to "197.200.0.1",
                    "deviceModel" to "Android DZ"
                )
            ).await()
        }
    }

    fun observeTransactions(): Flow<List<TransactionItem>> = flow {
        val uid = requireUserId()
        val path = "users/$uid/transactions"
        emitAll(
            db.collection("users").document(uid).collection("transactions")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .snapshots()
                .map { snapshot -> snapshot.toObjects(TransactionItem::class.java) }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    suspend fun sendUsdt(
        recipient: String,
        amount: Double,
        network: String = "TRC20",
        note: String = ""
    ): TransactionItem {
        val uid = requireUserId()
        val txCol = db.collection("users").document(uid).collection("transactions")
        val txId = "0x" + UUID.randomUUID().toString().replace("-", "").take(16)
        val fee = 1.0
        val payload = hashMapOf(
            "userId" to uid,
            "type" to "send",
            "amount" to amount,
            "currency" to "USDT",
            "network" to network,
            "recipient" to recipient,
            "sender" to "محفظتي",
            "txId" to txId,
            "status" to "confirmed",
            "fee" to fee,
            "counterpartAmount" to amount,
            "counterpartCurrency" to "USD",
            "note" to note,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        val doc = txCol.add(payload).await()
        // Deduct from profile balance
        val userRef = db.collection("users").document(uid)
        db.runTransaction { tx ->
            val snap = tx.get(userRef)
            val currentBalance = snap.getDouble("usdtBalance") ?: 1245.32
            val newBalance = (currentBalance - amount - fee).coerceAtLeast(0.0)
            tx.update(userRef, "usdtBalance", newBalance, "updatedAt", FieldValue.serverTimestamp())
        }.await()

        return TransactionItem(
            id = doc.id,
            userId = uid,
            type = "send",
            amount = amount,
            currency = "USDT",
            network = network,
            recipient = recipient,
            sender = "محفظتي",
            txId = txId,
            status = "confirmed",
            fee = fee,
            counterpartAmount = amount,
            counterpartCurrency = "USD",
            note = note
        )
    }

    suspend fun buyUsdt(amount: Double, paymentMethod: String): TransactionItem {
        val uid = requireUserId()
        val txCol = db.collection("users").document(uid).collection("transactions")
        val txId = "BUY-" + UUID.randomUUID().toString().take(8)
        val fee = amount * 0.025
        val payload = hashMapOf(
            "userId" to uid,
            "type" to "buy",
            "amount" to amount,
            "currency" to "USDT",
            "network" to "TRC20",
            "recipient" to "محفظتي",
            "sender" to paymentMethod,
            "txId" to txId,
            "status" to "confirmed",
            "fee" to fee,
            "counterpartAmount" to amount,
            "counterpartCurrency" to "USD",
            "note" to "شراء USDT عبر $paymentMethod",
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        val doc = txCol.add(payload).await()
        // Add to profile balance
        val userRef = db.collection("users").document(uid)
        db.runTransaction { tx ->
            val snap = tx.get(userRef)
            val currentBalance = snap.getDouble("usdtBalance") ?: 1245.32
            tx.update(userRef, "usdtBalance", currentBalance + amount, "updatedAt", FieldValue.serverTimestamp())
        }.await()

        return TransactionItem(
            id = doc.id,
            userId = uid,
            type = "buy",
            amount = amount,
            currency = "USDT",
            network = "TRC20",
            recipient = "محفظتي",
            sender = paymentMethod,
            txId = txId,
            status = "confirmed",
            fee = fee
        )
    }

    suspend fun exchangeCurrency(fromAmount: Double, toBtcAmount: Double): TransactionItem {
        val uid = requireUserId()
        val txCol = db.collection("users").document(uid).collection("transactions")
        val txId = "EXCH-" + UUID.randomUUID().toString().take(8)
        val fee = 1.0
        val payload = hashMapOf(
            "userId" to uid,
            "type" to "exchange",
            "amount" to fromAmount,
            "currency" to "USDT",
            "network" to "TRC20",
            "recipient" to "BTC Wallet",
            "sender" to "USDT Wallet",
            "txId" to txId,
            "status" to "confirmed",
            "fee" to fee,
            "counterpartAmount" to toBtcAmount,
            "counterpartCurrency" to "BTC",
            "note" to "تحويل من USDT إلى BTC",
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        val doc = txCol.add(payload).await()
        // Update user balances
        val userRef = db.collection("users").document(uid)
        db.runTransaction { tx ->
            val snap = tx.get(userRef)
            val currentUsdt = snap.getDouble("usdtBalance") ?: 1245.32
            val currentBtc = snap.getDouble("btcBalance") ?: 0.046
            tx.update(
                userRef,
                "usdtBalance", (currentUsdt - fromAmount - fee).coerceAtLeast(0.0),
                "btcBalance", currentBtc + toBtcAmount,
                "updatedAt", FieldValue.serverTimestamp()
            )
        }.await()

        return TransactionItem(
            id = doc.id,
            userId = uid,
            type = "exchange",
            amount = fromAmount,
            currency = "USDT",
            counterpartAmount = toBtcAmount,
            counterpartCurrency = "BTC",
            status = "confirmed"
        )
    }

    fun observeCards(): Flow<List<VirtualCardItem>> = flow {
        val uid = requireUserId()
        val path = "users/$uid/cards"
        emitAll(
            db.collection("users").document(uid).collection("cards")
                .snapshots()
                .map { snapshot -> snapshot.toObjects(VirtualCardItem::class.java) }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    suspend fun toggleCardFreeze(card: VirtualCardItem) {
        val uid = requireUserId()
        val newStatus = if (card.status == "active") "frozen" else "active"
        val docRef = db.collection("users").document(uid).collection("cards").document(card.id)
        docRef.update("status", newStatus, "updatedAt", FieldValue.serverTimestamp()).await()
    }

    suspend fun toggleOnlinePayment(card: VirtualCardItem) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("cards").document(card.id)
        docRef.update("onlinePaymentEnabled", !card.onlinePaymentEnabled, "updatedAt", FieldValue.serverTimestamp()).await()
    }

    fun observeNotifications(): Flow<List<NotificationItem>> = flow {
        val uid = requireUserId()
        val path = "users/$uid/notifications"
        emitAll(
            db.collection("users").document(uid).collection("notifications")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .snapshots()
                .map { snapshot -> snapshot.toObjects(NotificationItem::class.java) }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    suspend fun recordConsent(version: String = "1.0", accepted: Boolean = true) {
        val uid = requireUserId()
        val consentCol = db.collection("users").document(uid).collection("consent_records")
        consentCol.add(
            hashMapOf(
                "userId" to uid,
                "policyVersion" to version,
                "termsAccepted" to accepted,
                "acceptedAt" to FieldValue.serverTimestamp(),
                "ipAddress" to "197.200.0.1",
                "deviceModel" to "Android Mobile"
            )
        ).await()
    }
}
