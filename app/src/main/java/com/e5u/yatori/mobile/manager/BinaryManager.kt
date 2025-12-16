package com.e5u.yatori.mobile.manager

import android.content.Context
import android.os.Build
import com.e5u.yatori.mobile.R
import java.io.File
import java.io.FileOutputStream

/**
 * Manager for handling binary file operations
 */
class BinaryManager(private val context: Context) {

    private val binaryDir: File = context.filesDir
    private val binaryName = "yatori-go-console"

    /**
     * Get the binary file path
     */
    fun getBinaryPath(): String {
        return File(binaryDir, binaryName).absolutePath
    }

    /**
     * Check if binary exists
     */
    fun isBinaryAvailable(): Boolean {
        val binaryFile = File(binaryDir, binaryName)
        return binaryFile.exists() && binaryFile.canExecute()
    }

    /**
     * Detect device architecture
     */
    fun getDeviceArchitecture(): String {
        val abis = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Build.SUPPORTED_ABIS
        } else {
            @Suppress("DEPRECATION")
            arrayOf(Build.CPU_ABI, Build.CPU_ABI2)
        }

        return when {
            abis.any { it.startsWith("arm64") || it.startsWith("aarch64") } -> "arm64"
            abis.any { it.startsWith("armeabi") } -> "arm"
            abis.any { it.startsWith("x86_64") } -> "x86_64"
            abis.any { it.startsWith("x86") } -> "x86"
            else -> "unknown"
        }
    }

    /**
     * Extract binary from assets
     */
    fun extractBinaryFromAssets(): Result<String> {
        return try {
            val arch = getDeviceArchitecture()
            
            // Try to find architecture-specific binary first
            val assetName = "$binaryName-$arch"
            val assetList = context.assets.list("") ?: emptyArray()
            
            val binaryAsset = when {
                assetList.contains(assetName) -> assetName
                assetList.contains(binaryName) -> binaryName
                else -> return Result.failure(
                    Exception(context.getString(R.string.error_binary_not_found))
                )
            }

            // Extract binary
            context.assets.open(binaryAsset).use { input ->
                val outputFile = File(binaryDir, binaryName)
                FileOutputStream(outputFile).use { output ->
                    input.copyTo(output)
                }
                
                // Set executable permissions
                if (!outputFile.setExecutable(true, false)) {
                    return Result.failure(
                        Exception(context.getString(R.string.error_binary_permissions))
                    )
                }
                
                Result.success(outputFile.absolutePath)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Ensure binary is extracted and ready
     */
    suspend fun ensureBinaryExtracted(): Result<String> {
        return if (isBinaryAvailable()) {
            Result.success(getBinaryPath())
        } else {
            extractBinaryFromAssets()
        }
    }

    /**
     * Verify binary integrity
     */
    fun verifyBinary(): Boolean {
        val binaryFile = File(binaryDir, binaryName)
        return binaryFile.exists() && 
               binaryFile.canExecute() && 
               binaryFile.length() > 0
    }

    /**
     * Delete binary file
     */
    fun deleteBinary(): Boolean {
        val binaryFile = File(binaryDir, binaryName)
        return if (binaryFile.exists()) {
            binaryFile.delete()
        } else {
            true
        }
    }
}
