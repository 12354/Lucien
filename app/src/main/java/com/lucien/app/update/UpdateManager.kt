package com.lucien.app.update

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

data class UpdateInfo(
    val remoteVersionCode: Int,
    val currentVersionCode: Int
) {
    val hasUpdate: Boolean get() = remoteVersionCode > currentVersionCode
}

class UpdateManager(private val context: Context) {

    companion object {
        private const val VERSION_URL =
            "https://github.com/12354/Lucien/releases/download/latest-debug/version.txt"
        private const val APK_URL =
            "https://github.com/12354/Lucien/releases/download/latest-debug/lucien-debug.apk"
    }

    private fun getCurrentVersionCode(): Int {
        return try {
            val info = context.packageManager.getPackageInfo(context.packageName, 0)
            @Suppress("DEPRECATION")
            info.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            0
        }
    }

    suspend fun checkForUpdate(): UpdateInfo? = withContext(Dispatchers.IO) {
        try {
            val connection = URL(VERSION_URL).openConnection() as HttpURLConnection
            connection.connectTimeout = 10_000
            connection.readTimeout = 10_000
            connection.instanceFollowRedirects = true
            try {
                val remoteVersion = connection.inputStream.bufferedReader()
                    .readText().trim().toInt()
                UpdateInfo(
                    remoteVersionCode = remoteVersion,
                    currentVersionCode = getCurrentVersionCode()
                )
            } finally {
                connection.disconnect()
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun downloadUpdate(onProgress: (Float) -> Unit): File? = withContext(Dispatchers.IO) {
        try {
            val updateDir = File(context.cacheDir, "updates")
            updateDir.mkdirs()
            val apkFile = File(updateDir, "lucien-update.apk")
            if (apkFile.exists()) apkFile.delete()

            val connection = URL(APK_URL).openConnection() as HttpURLConnection
            connection.connectTimeout = 15_000
            connection.readTimeout = 30_000
            connection.instanceFollowRedirects = true
            try {
                val totalBytes = connection.contentLength.toLong()
                var downloadedBytes = 0L

                connection.inputStream.use { input ->
                    apkFile.outputStream().use { output ->
                        val buffer = ByteArray(8192)
                        var bytesRead: Int
                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            output.write(buffer, 0, bytesRead)
                            downloadedBytes += bytesRead
                            if (totalBytes > 0) {
                                onProgress(downloadedBytes.toFloat() / totalBytes)
                            }
                        }
                    }
                }
                onProgress(1f)
                apkFile
            } finally {
                connection.disconnect()
            }
        } catch (e: Exception) {
            null
        }
    }

    fun installApk(apkFile: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            apkFile
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        context.startActivity(intent)
    }
}
