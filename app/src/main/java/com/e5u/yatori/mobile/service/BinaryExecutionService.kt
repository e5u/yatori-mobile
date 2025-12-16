package com.e5u.yatori.mobile.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import com.e5u.yatori.mobile.YatoriApplication
import com.e5u.yatori.mobile.util.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Foreground service for executing the binary in background
 */
class BinaryExecutionService : LifecycleService() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    private var executionJob: Job? = null
    private var process: Process? = null

    private val app: YatoriApplication
        get() = application as YatoriApplication

    override fun onCreate() {
        super.onCreate()
        
        // Start as foreground service
        val notification = NotificationHelper.createRunningNotification(this)
        startForeground(NotificationHelper.NOTIFICATION_ID_EXECUTION, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        when (intent?.action) {
            ACTION_START_EXECUTION -> startExecution()
            ACTION_STOP_EXECUTION -> stopExecution()
        }

        return START_STICKY
    }

    /**
     * Start binary execution
     */
    private fun startExecution() {
        if (executionJob?.isActive == true) {
            app.logManager.writeLogSync("Execution already in progress")
            return
        }

        executionJob = serviceScope.launch {
            try {
                val binaryPath = app.binaryManager.getBinaryPath()
                app.logManager.writeLog("Starting execution: $binaryPath")

                // Execute binary
                val processBuilder = ProcessBuilder(binaryPath)
                processBuilder.redirectErrorStream(true)
                process = processBuilder.start()

                // Read output
                val reader = BufferedReader(InputStreamReader(process!!.inputStream))
                var line: String?
                
                while (isActive && reader.readLine().also { line = it } != null) {
                    line?.let { app.logManager.writeLog(it) }
                }

                // Wait for process to complete
                val exitCode = process?.waitFor() ?: -1
                app.logManager.writeLog("Execution completed with exit code: $exitCode")

                if (exitCode == 0) {
                    showCompletedNotification()
                } else {
                    showFailedNotification("Exit code: $exitCode")
                }
            } catch (e: Exception) {
                app.logManager.writeLog("Execution failed: ${e.message}")
                showFailedNotification(e.message ?: "Unknown error")
            } finally {
                stopSelf()
            }
        }
    }

    /**
     * Stop binary execution
     */
    private fun stopExecution() {
        try {
            process?.destroy()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                process?.destroyForcibly()
            }
            executionJob?.cancel()
            app.logManager.writeLogSync("Execution stopped by user")
        } catch (e: Exception) {
            app.logManager.writeLogSync("Error stopping execution: ${e.message}")
        } finally {
            stopSelf()
        }
    }

    /**
     * Show completed notification
     */
    private fun showCompletedNotification() {
        val notification = NotificationHelper.createCompletedNotification(this)
        NotificationHelper.showNotification(
            this,
            NotificationHelper.NOTIFICATION_ID_COMPLETED,
            notification
        )
    }

    /**
     * Show failed notification
     */
    private fun showFailedNotification(error: String) {
        val notification = NotificationHelper.createFailedNotification(this, error)
        NotificationHelper.showNotification(
            this,
            NotificationHelper.NOTIFICATION_ID_FAILED,
            notification
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        stopExecution()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    companion object {
        const val ACTION_START_EXECUTION = "com.e5u.yatori.mobile.START_EXECUTION"
        const val ACTION_STOP_EXECUTION = "com.e5u.yatori.mobile.STOP_EXECUTION"
    }
}
