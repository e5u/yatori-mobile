package com.e5u.yatori.mobile.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.e5u.yatori.mobile.R
import com.e5u.yatori.mobile.YatoriApplication
import com.e5u.yatori.mobile.databinding.ActivityLogBinding
import kotlinx.coroutines.launch

/**
 * Activity for viewing and managing logs
 */
class LogViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogBinding
    private val app: YatoriApplication
        get() = application as YatoriApplication

    private var fullLogText: String = ""
    private var isSearchVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupUI()
        loadLogs()
    }

    /**
     * Setup UI components
     */
    private fun setupUI() {
        binding.fab.setOnClickListener {
            exportLogs()
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    /**
     * Load logs from file
     */
    private fun loadLogs() {
        lifecycleScope.launch {
            val logs = app.logManager.readLogs()
            fullLogText = logs
            
            if (logs.isEmpty()) {
                binding.emptyView.visibility = android.view.View.VISIBLE
                binding.logCard.visibility = android.view.View.GONE
            } else {
                binding.emptyView.visibility = android.view.View.GONE
                binding.logCard.visibility = android.view.View.VISIBLE
                binding.logTextView.text = logs
                
                // Scroll to bottom
                binding.logTextView.post {
                    val scrollView = binding.logTextView.parent.parent as? androidx.core.widget.NestedScrollView
                    scrollView?.fullScroll(android.view.View.FOCUS_DOWN)
                }
            }
        }
    }

    /**
     * Export logs
     */
    private fun exportLogs() {
        lifecycleScope.launch {
            val result = app.logManager.exportLogs()
            
            if (result.isSuccess) {
                startActivity(result.getOrNull())
            } else {
                Toast.makeText(
                    this@LogViewerActivity,
                    getString(R.string.error_log_export_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Clear logs with confirmation
     */
    private fun clearLogs() {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_title_confirm)
            .setMessage(R.string.dialog_message_clear_logs)
            .setPositiveButton(R.string.button_yes) { _, _ ->
                lifecycleScope.launch {
                    app.logManager.clearLogs()
                    loadLogs()
                    Toast.makeText(
                        this@LogViewerActivity,
                        R.string.label_logs_cleared,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton(R.string.button_no, null)
            .show()
    }

    /**
     * Toggle search bar visibility
     */
    private fun toggleSearch() {
        isSearchVisible = !isSearchVisible
        binding.searchBar.visibility = if (isSearchVisible) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_log_viewer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                toggleSearch()
                true
            }
            R.id.action_clear -> {
                clearLogs()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        loadLogs()
    }
}
