package com.example

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class DZWalletApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                val res = resources
                val appIdResId = res.getIdentifier("google_app_id", "string", packageName)
                val projectIdResId = res.getIdentifier("project_id", "string", packageName)
                val apiKeyResId = res.getIdentifier("google_api_key", "string", packageName)

                val appId = if (appIdResId != 0) getString(appIdResId) else "1:123456789012:android:abcdef0123456789abcdef"
                val projectId = if (projectIdResId != 0) getString(projectIdResId) else "dz-wallet-app"
                val apiKey = if (apiKeyResId != 0) getString(apiKeyResId) else "AIzaSyFakeKeyForDzWalletAndroidApplication"

                val options = FirebaseOptions.Builder()
                    .setApplicationId(appId)
                    .setProjectId(projectId)
                    .setApiKey(apiKey)
                    .build()

                FirebaseApp.initializeApp(this, options)
                Log.d("DZWalletApp", "FirebaseApp successfully initialized.")
            }
        } catch (e: Exception) {
            Log.e("DZWalletApp", "FirebaseApp initialization failed", e)
        }
    }
}
