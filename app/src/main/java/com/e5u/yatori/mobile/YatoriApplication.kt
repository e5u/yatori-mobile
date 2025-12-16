package com.e5u.yatori.mobile

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.e5u.yatori.mobile.manager.BinaryManager
import com.e5u.yatori.mobile.manager.LogManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Application class for Yatori Mobile.
 * Handles app-wide initialization including notification channels,
 * binary extraction, and manager setup.
 */
class YatoriApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    lateinit var binaryManager: BinaryManager
        private set

    lateinit var logManager: LogManager
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Initialize managers
        binaryManager = BinaryManager(this)
        logManager = LogManager(this)

        // Create notification channels
        createNotificationChannels()

        // Initialize binary in background
        applicationScope.launch(Dispatchers.IO) {
            try {
                binaryManager.ensureBinaryExtracted()
            } catch (e: Exception) {
                logManager.writeLog("Failed to extract binary on startup: ${e.message}")
            }
        }
    }

    /**
     * Create notification channels for Android O and above
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val descriptionText = getString(R.string.notification_channel_desc)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "yatori_execution_channel"

        lateinit var instance: YatoriApplication
            private set
    }
}
