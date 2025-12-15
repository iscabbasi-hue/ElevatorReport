package com.example.elevatorrportmahestan.data.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

fun enqueueReportSync(context: Context, reportId: String) {

    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val work = OneTimeWorkRequestBuilder<ReportSyncWorker>()
        .setInputData(
            workDataOf("REPORT_ID" to reportId)
        )
        .setConstraints(constraints)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            30,
            TimeUnit.SECONDS
        )
        .build()

    WorkManager
        .getInstance(context.applicationContext)
        .enqueueUniqueWork(
            "sync_$reportId",
            ExistingWorkPolicy.REPLACE,
            work
        )
}
