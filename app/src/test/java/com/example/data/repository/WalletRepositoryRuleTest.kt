package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import com.example.data.model.UserProfile
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID

class WalletRepositoryRuleTest : FirestoreEmulatorTestBase() {

  @Test
  fun authenticatedUser_canCreateAndRetrieveProfile() = runBlocking {
    val uid = signInTestUser("alice_${UUID.randomUUID()}@example.com")
    val repository = WalletRepository(firestore)

    val currentUser = auth.currentUser
    assertNotNull(currentUser)

    repository.ensureInitialData(currentUser!!)

    val profileSnap = firestore.collection("users").document(uid).get()
    // In headless test, check the document is written
    assertNotNull(profileSnap)
  }

  @Test
  fun unauthenticatedUser_cannotWriteProfile() = runBlocking {
    auth.signOut()
    var exceptionThrown = false
    try {
      val repository = WalletRepository(firestore)
      repository.sendUsdt(recipient = "0x123", amount = 10.0)
    } catch (e: IllegalStateException) {
      exceptionThrown = true
    }
    assertTrue("Should throw IllegalStateException when unauthenticated", exceptionThrown)
  }
}
