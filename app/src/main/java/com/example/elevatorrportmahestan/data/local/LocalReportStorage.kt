package com.example.elevatorrportmahestan.data.local

import android.content.Context
import com.example.elevatorrportmahestan.model.Report
import com.example.elevatorrportmahestan.model.ReportStatus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object LocalReportStorage {

    private const val PREF_NAME = "local_reports"
    private const val KEY_REPORTS = "reports"

    private val gson = Gson()

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun save(context: Context, report: Report) {
        val reports = getAll(context).toMutableList()
        reports.add(report)
        prefs(context).edit()
            .putString(KEY_REPORTS, gson.toJson(reports))
            .apply()
    }

    fun getAll(context: Context): List<Report> {
        val json = prefs(context).getString(KEY_REPORTS, null) ?: return emptyList()
        val type = object : TypeToken<List<Report>>() {}.type
        return gson.fromJson(json, type)
    }

    fun getById(context: Context, id: String): Report? {
        return getAll(context).find { it.id == id }
    }

    fun markAsSent(context: Context, id: String) {
        val updated = getAll(context).map {
            if (it.id == id) it.copy(status = ReportStatus.SENT)
            else it
        }
        prefs(context).edit()
            .putString(KEY_REPORTS, gson.toJson(updated))
            .apply()
    }

    fun remove(context: Context, id: String) {
        val updated = getAll(context).filterNot { it.id == id }
        prefs(context).edit()
            .putString(KEY_REPORTS, gson.toJson(updated))
            .apply()
    }
}
