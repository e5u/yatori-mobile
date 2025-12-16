package com.e5u.yatori.mobile.manager

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Manager for handling log operations
 */
class LogManager(private val context: Context) {

    private val logDir: File = File(context.filesDir, "logs")
    private val currentLogFile: File
        get() = File(logDir, "yatori_${getCurrentDate()}.log")

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val timestampFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    init {
        // Ensure log directory exists
        if (!logDir.exists()) {
            logDir.mkdirs()
        }
    }

    /**
     * Get current date string for log file naming
     */
    private fun getCurrentDate(): String {
        return dateFormat.format(Date())
    }

    /**
     * Get current timestamp for log entries
     */
    private fun getCurrentTimestamp(): String {
        return timestampFormat.format(Date())
    }

    /**
     * Write log message
     */
    suspend fun writeLog(message: String) = withContext(Dispatchers.IO) {
        try {
            val timestamp = getCurrentTimestamp()
            val logEntry = "[$timestamp] $message\n"
            currentLogFile.appendText(logEntry)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Write log message synchronously
     */
    fun writeLogSync(message: String) {
        try {
            val timestamp = getCurrentTimestamp()
            val logEntry = "[$timestamp] $message\n"
            currentLogFile.appendText(logEntry)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Read all logs from current log file
     */
    suspend fun readLogs(): String = withContext(Dispatchers.IO) {
        try {
            if (currentLogFile.exists()) {
                currentLogFile.readText()
            } else {
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * Read logs from a specific date
     */
    suspend fun readLogsForDate(date: String): String = withContext(Dispatchers.IO) {
        try {
            val file = File(logDir, "yatori_$date.log")
            if (file.exists()) {
                file.readText()
            } else {
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * Get all log files
     */
    fun getAllLogFiles(): List<File> {
        return logDir.listFiles()?.filter { it.name.endsWith(".log") }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()
    }

    /**
     * Clear current log file
     */
    suspend fun clearLogs() = withContext(Dispatchers.IO) {
        try {
            if (currentLogFile.exists()) {
                currentLogFile.writeText("")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Clear all log files
     */
    suspend fun clearAllLogs() = withContext(Dispatchers.IO) {
        try {
            logDir.listFiles()?.forEach { file ->
                if (file.name.endsWith(".log")) {
                    file.delete()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Export logs to external storage
     */
    suspend fun exportLogs(): Result<Intent> = withContext(Dispatchers.IO) {
        try {
            if (!currentLogFile.exists() || currentLogFile.length() == 0L) {
                return@withContext Result.failure(Exception("No logs to export"))
            }

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                currentLogFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Yatori Logs - ${getCurrentDate()}")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            Result.success(Intent.createChooser(shareIntent, "Export Logs"))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Delete old log files (keep only last N days)
     */
    suspend fun deleteOldLogs(daysToKeep: Int) = withContext(Dispatchers.IO) {
        try {
            val cutoffTime = System.currentTimeMillis() - (daysToKeep * 24 * 60 * 60 * 1000L)
            logDir.listFiles()?.forEach { file ->
                if (file.name.endsWith(".log") && file.lastModified() < cutoffTime) {
                    file.delete()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Get total log size in bytes
     */
    fun getTotalLogSize(): Long {
        return logDir.listFiles()?.filter { it.name.endsWith(".log") }
            ?.sumOf { it.length() }
            ?: 0L
    }

    /**
     * Format log size to human-readable string
     */
    fun formatLogSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> String.format("%.2f KB", bytes / 1024.0)
            bytes < 1024 * 1024 * 1024 -> String.format("%.2f MB", bytes / (1024.0 * 1024))
            else -> String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024))
        }
    }
}
