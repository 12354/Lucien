package com.lucien.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lucien.app.ui.theme.LucienTheme
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val crashDir = File(
            android.os.Environment.getExternalStoragePublicDirectory(
                android.os.Environment.DIRECTORY_DOWNLOADS
            ),
            "Lucien"
        )
        crashDir.mkdirs()
        val logFile = File(crashDir, "lucien-crash.log")

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            val sw = StringWriter()
            throwable.printStackTrace(PrintWriter(sw))
            val stackTrace = sw.toString()
            Log.e("LucienCrash", "Uncaught exception: $stackTrace")
            try {
                logFile.writeText(
                    "=== Lucien Crash Log ===\n" +
                    "Time: ${java.util.Date()}\n" +
                    "Thread: ${thread.name}\n" +
                    "Exception: ${throwable.javaClass.name}\n" +
                    "Message: ${throwable.message}\n\n" +
                    "Stack trace:\n$stackTrace\n\n" +
                    "Cause chain:\n" +
                    generateCauseChain(throwable)
                )
            } catch (e: Exception) {
                Log.e("LucienCrash", "Failed to write crash log: ${e.message}")
            }
            android.os.Process.killProcess(android.os.Process.myPid())
        }

        setContent {
            LucienTheme {
                LucienApp()
            }
        }
    }

    private fun generateCauseChain(throwable: Throwable): String {
        val sb = StringBuilder()
        var cause: Throwable? = throwable.cause
        var depth = 0
        while (cause != null && depth < 10) {
            val sw = StringWriter()
            cause.printStackTrace(PrintWriter(sw))
            sb.append("--- Cause ${depth + 1}: ${cause.javaClass.name} ---\n")
            sb.append("Message: ${cause.message}\n")
            sb.append(sw.toString())
            sb.append("\n")
            cause = cause.cause
            depth++
        }
        return sb.toString()
    }
}
