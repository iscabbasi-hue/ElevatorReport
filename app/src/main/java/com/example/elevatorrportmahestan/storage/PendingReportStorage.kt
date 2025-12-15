package com.example.elevatorrportmahestan.storage

import android.content.Context
import com.example.elevatorrportmahestan.model.Report
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PendingReportStorage {

    private const val PREF_NAME = "pending_reports_pref"
    private const val KEY_PENDING_REPORTS = "pending_reports"

    private val gson = Gson()

    fun load(context: Context): MutableList<Report> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_PENDING_REPORTS, null) ?: return mutableListOf()

        return try {
            val type = object : TypeToken<MutableList<Report>>() {}.type
            gson.fromJson(json, type) ?: mutableListOf()
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    fun save(context: Context, list: List<Report>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = gson.toJson(list)
        prefs.edit().putString(KEY_PENDING_REPORTS, json).apply()
    }

    fun clear(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_PENDING_REPORTS).apply()
    }
}
