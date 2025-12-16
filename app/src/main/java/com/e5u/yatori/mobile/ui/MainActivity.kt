package com.e5u.yatori.mobile.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.e5u.yatori.mobile.R
import com.e5u.yatori.mobile.YatoriApplication
import com.e5u.yatori.mobile.databinding.ActivityMainBinding
import com.e5u.yatori.mobile.service.BinaryExecutionService
import com.e5u.yatori.mobile.util.PermissionHelper
import kotlinx.coroutines.launch

/**
 * Main activity - entry point of the application
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val app: YatoriApplication
        get() = application as YatoriApplication

    private var isExecuting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        
        setupUI()
        checkPermissions()
        checkBinaryStatus()
    }

    /**
     * Setup UI components
     */
    private fun setupUI() {
        binding.fab.setOnClickListener {
            if (isExecuting) {
                stopExecution()
            } else {
                startExecution()
            }
        }
    }

    /**
     * Check and request necessary permissions
     */
    private fun checkPermissions() {
        if (!PermissionHelper.hasStoragePermission(this)) {
            if (PermissionHelper.shouldShowStoragePermissionRationale(this)) {
                showPermissionRationale(
                    getString(R.string.permission_title_storage),
                    getString(R.string.permission_message_storage)
                ) {
                    PermissionHelper.requestStoragePermission(this)
                }
            } else {
                PermissionHelper.requestStoragePermission(this)
            }
        }

        if (!PermissionHelper.hasNotificationPermission(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                PermissionHelper.requestNotificationPermission(this)
            }
        }
    }

    /**
     * Show permission rationale dialog
     */
    private fun showPermissionRationale(title: String, message: String, onAccept: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.button_grant_permission) { _, _ -> onAccept() }
            .setNegativeButton(R.string.button_cancel, null)
            .show()
    }

    /**
     * Check binary status
     */
    private fun checkBinaryStatus() {
        binding.statusProgress.visibility = android.view.View.VISIBLE
        binding.binaryStatusText.text = getString(R.string.status_binary_checking)

        lifecycleScope.launch {
            val result = app.binaryManager.ensureBinaryExtracted()
            
            binding.statusProgress.visibility = android.view.View.GONE
            
            if (result.isSuccess) {
                binding.binaryStatusText.text = getString(R.string.status_binary_ready)
                binding.statusIcon.setImageResource(android.R.drawable.presence_online)
                binding.statusIcon.setColorFilter(
                    ContextCompat.getColor(this@MainActivity, R.color.status_success)
                )
                app.logManager.writeLog("Binary ready at: ${result.getOrNull()}")
            } else {
                binding.binaryStatusText.text = getString(R.string.status_binary_missing)
                binding.statusIcon.setImageResource(android.R.drawable.presence_busy)
                binding.statusIcon.setColorFilter(
                    ContextCompat.getColor(this@MainActivity, R.color.status_error)
                )
                app.logManager.writeLog("Binary check failed: ${result.exceptionOrNull()?.message}")
                
                Toast.makeText(
                    this@MainActivity,
                    result.exceptionOrNull()?.message ?: getString(R.string.error_binary_not_found),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Start binary execution
     */
    private fun startExecution() {
        if (!app.binaryManager.isBinaryAvailable()) {
            Toast.makeText(this, R.string.error_binary_not_found, Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, BinaryExecutionService::class.java).apply {
            action = BinaryExecutionService.ACTION_START_EXECUTION
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }

        isExecuting = true
        updateExecutionUI()
        
        Toast.makeText(this, R.string.message_execution_started, Toast.LENGTH_SHORT).show()
    }

    /**
     * Stop binary execution
     */
    private fun stopExecution() {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_title_confirm)
            .setMessage(R.string.dialog_message_stop_execution)
            .setPositiveButton(R.string.button_yes) { _, _ ->
                val intent = Intent(this, BinaryExecutionService::class.java).apply {
                    action = BinaryExecutionService.ACTION_STOP_EXECUTION
                }
                startService(intent)
                
                isExecuting = false
                updateExecutionUI()
                
                Toast.makeText(this, R.string.message_execution_stopped, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.button_no, null)
            .show()
    }

    /**
     * Update UI based on execution state
     */
    private fun updateExecutionUI() {
        if (isExecuting) {
            binding.executionStatusText.text = getString(R.string.status_running)
            binding.fab.setImageResource(android.R.drawable.ic_media_pause)
        } else {
            binding.executionStatusText.text = getString(R.string.status_stopped)
            binding.fab.setImageResource(android.R.drawable.ic_media_play)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_view_logs -> {
                startActivity(Intent(this, LogViewerActivity::class.java))
                true
            }
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        when (requestCode) {
            PermissionHelper.REQUEST_CODE_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this,
                        R.string.error_no_storage_permission,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            PermissionHelper.REQUEST_CODE_NOTIFICATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
