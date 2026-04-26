package com.lucien.app.ui.screens.poop

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.mutableStateListOf
import org.json.JSONArray
import org.json.JSONObject

enum class PoopSize(val label: String) {
    SMALL("Small"),
    MEDIUM("Medium"),
    LARGE("Large")
}

enum class PoopEase(val label: String) {
    EASY("Easy"),
    OK("OK"),
    DIFFICULT("Difficult")
}

data class PoopLog(
    val id: Long,
    val timestamp: Long,
    val size: PoopSize? = null,
    val ease: PoopEase? = null
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("timestamp", timestamp)
        size?.let { put("size", it.name) }
        ease?.let { put("ease", it.name) }
    }

    companion object {
        fun fromJson(obj: JSONObject): PoopLog = PoopLog(
            id = obj.getLong("id"),
            timestamp = obj.getLong("timestamp"),
            size = obj.optString("size", "").takeIf { it.isNotEmpty() }
                ?.let { runCatching { PoopSize.valueOf(it) }.getOrNull() },
            ease = obj.optString("ease", "").takeIf { it.isNotEmpty() }
                ?.let { runCatching { PoopEase.valueOf(it) }.getOrNull() }
        )
    }
}

class PoopLogRepository private constructor(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    val logs: SnapshotStateList<PoopLog> = mutableStateListOf<PoopLog>().apply {
        addAll(loadFromPrefs())
    }

    fun add(log: PoopLog) {
        val insertAt = logs.indexOfFirst { it.timestamp <= log.timestamp }
            .let { if (it == -1) logs.size else it }
        logs.add(insertAt, log)
        persist()
    }

    fun remove(id: Long) {
        if (logs.removeAll { it.id == id }) persist()
    }

    private fun persist() {
        val arr = JSONArray()
        logs.forEach { arr.put(it.toJson()) }
        prefs.edit().putString(KEY_LOGS, arr.toString()).apply()
    }

    private fun loadFromPrefs(): List<PoopLog> {
        val raw = prefs.getString(KEY_LOGS, null) ?: return emptyList()
        return try {
            val arr = JSONArray(raw)
            buildList {
                for (i in 0 until arr.length()) add(PoopLog.fromJson(arr.getJSONObject(i)))
            }.sortedByDescending { it.timestamp }
        } catch (e: Exception) {
            emptyList()
        }
    }

    companion object {
        private const val PREFS_NAME = "poop_logger"
        private const val KEY_LOGS = "logs"

        @Volatile private var instance: PoopLogRepository? = null

        fun get(context: Context): PoopLogRepository =
            instance ?: synchronized(this) {
                instance ?: PoopLogRepository(context.applicationContext).also { instance = it }
            }
    }
}
