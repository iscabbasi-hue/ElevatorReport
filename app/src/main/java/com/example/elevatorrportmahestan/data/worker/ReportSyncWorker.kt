package com.example.elevatorrportmahestan.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.elevatorrportmahestan.model.toFirestoreModel
import com.example.elevatorrportmahestan.storage.PendingReportStorage
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReportSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val reportId = inputData.getString("REPORT_ID")
            ?: return Result.failure()

        val pendingList = PendingReportStorage.load(applicationContext)

        val report = pendingList.firstOrNull { it.id == reportId }
            ?: return Result.success() // قبلاً ارسال شده

        return try {

            val firestoreModel = report.toFirestoreModel()

            FirebaseFirestore.getInstance()
                .collection("reports")
                .document(report.id)
                .set(firestoreModel)
                .await()

            // ✅ حذف از Pending بعد از ارسال موفق
            pendingList.removeAll { it.id == report.id }
            PendingReportStorage.save(applicationContext, pendingList)

            Result.success()

        } catch (e: Exception) {
            Result.retry()
        }
    }
}
