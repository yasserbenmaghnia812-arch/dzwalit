package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class UserProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val kycStatus: String = "verified", // "not_verified", "in_review", "verified", "rejected"
    val kycRejectionReason: String? = null,
    val biometricEnabled: Boolean = true,
    val twoFactorEnabled: Boolean = true,
    val pinEnabled: Boolean = true,
    val pinCode: String = "1234",
    val privacyPolicyAccepted: Boolean = true,
    @ServerTimestamp val privacyPolicyAcceptedAt: Timestamp? = null,
    val language: String = "ar",
    val usdtBalance: Double = 1245.32,
    val btcBalance: Double = 0.046,
    val dzdBalance: Double = 311330.0,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class TransactionItem(
    @DocumentId val id: String = "",
    val userId: String = "",
    val type: String = "receive", // "send", "receive", "buy", "exchange", "card_payment", "fee"
    val amount: Double = 0.0,
    val currency: String = "USDT",
    val network: String = "TRC20",
    val recipient: String = "",
    val sender: String = "",
    val txId: String = "",
    val status: String = "confirmed", // "created", "pending", "submitted", "confirming", "confirmed", "failed", "cancelled"
    val fee: Double = 1.0,
    val counterpartAmount: Double = 0.0,
    val counterpartCurrency: String = "USD",
    val note: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class VirtualCardItem(
    @DocumentId val id: String = "",
    val userId: String = "",
    val cardHolder: String = "AHMED BENALI",
    val last4: String = "4821",
    val cardType: String = "Virtual Card - VISA",
    val status: String = "active", // "active", "frozen", "pending"
    val spendingLimit: Double = 5000.0,
    val availableBalance: Double = 1245.32,
    val onlinePaymentEnabled: Boolean = true,
    val contactlessEnabled: Boolean = true,
    val expiryDate: String = "12/28",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class NotificationItem(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val body: String = "",
    val type: String = "transfer", // "transfer", "security", "order", "system"
    val read: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class ConsentRecordItem(
    @DocumentId val id: String = "",
    val userId: String = "",
    val policyVersion: String = "1.0",
    val termsAccepted: Boolean = true,
    @ServerTimestamp val acceptedAt: Timestamp? = null,
    val ipAddress: String = "197.200.0.1",
    val deviceModel: String = "Android Device"
)
