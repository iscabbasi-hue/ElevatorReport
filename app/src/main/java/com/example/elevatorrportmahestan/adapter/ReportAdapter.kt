package com.example.elevatorrportmahestan.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import com.example.elevatorrportmahestan.R
import com.example.elevatorrportmahestan.model.Report
import com.example.elevatorrportmahestan.model.ReportStatus
import java.util.Calendar
import java.util.Date

class ReportDiffCallback : DiffUtil.ItemCallback<Report>() {

    override fun areItemsTheSame(oldItem: Report, newItem: Report): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Report, newItem: Report): Boolean {
        return oldItem == newItem
    }
}

class ReportAdapter :
    ListAdapter<Report, ReportAdapter.ReportViewHolder>(ReportDiffCallback()) {

    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val errorCode: TextView = itemView.findViewById(R.id.tvErrorCode)
        val visualProblem: TextView = itemView.findViewById(R.id.tvVisualProblem)
        val actions: TextView = itemView.findViewById(R.id.tvActions)
        val parts: TextView = itemView.findViewById(R.id.tvParts)
        val status: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = getItem(position)

        holder.txtDate.text = getPersianDate(report.timestamp)
        holder.errorCode.text = "کد خطا: ${report.errorCode}"
        holder.visualProblem.text = "مشکل: ${report.visualProblem}"
        holder.actions.text = "اقدامات: ${report.actions}"
        holder.parts.text = "قطعات: ${report.parts}"

        when (report.status) {
            ReportStatus.PENDING -> {
                holder.status.text = "در حال ارسال"
                holder.status.setTextColor(Color.RED)
            }
            ReportStatus.SENT -> {
                holder.status.text = "ارسال شده"
                holder.status.setTextColor(Color.parseColor("#2E7D32"))
            }
        }
    }

    // ✅ تبدیل تاریخ میلادی به شمسی (بدون کتابخانه)
    private fun getPersianDate(timeMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.time = Date(timeMillis)

        val gy = calendar.get(Calendar.YEAR)
        val gm = calendar.get(Calendar.MONTH) + 1
        val gd = calendar.get(Calendar.DAY_OF_MONTH)

        val g_d_m = intArrayOf(
            0, 31, 59, 90, 120, 151, 181,
            212, 243, 273, 304, 334
        )

        var gy2 = if (gm > 2) gy + 1 else gy
        var days = 355666 + (365 * gy)
        days += (gy2 + 3) / 4
        days -= (gy2 + 99) / 100
        days += (gy2 + 399) / 400
        days += gd + g_d_m[gm - 1]

        var jy = -1595 + (33 * (days / 12053))
        days %= 12053
        jy += 4 * (days / 1461)
        days %= 1461

        if (days > 365) {
            jy += (days - 1) / 365
            days = (days - 1) % 365
        }

        val jm = if (days < 186) 1 + days / 31 else 7 + (days - 186) / 30
        val jd = 1 + if (days < 186) days % 31 else (days - 186) % 30

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return "%04d/%02d/%02d - %02d:%02d".format(jy, jm, jd, hour, minute)
    }
}
