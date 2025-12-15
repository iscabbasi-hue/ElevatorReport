package com.example.elevatorrportmahestan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elevatorrportmahestan.adapter.ReportAdapter
import com.example.elevatorrportmahestan.model.PendingSource
import com.example.elevatorrportmahestan.model.Report
import com.example.elevatorrportmahestan.model.ReportStatus
import com.example.elevatorrportmahestan.storage.PendingReportStorage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.example.elevatorrportmahestan.model.FirestoreReport


class ReportListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReportAdapter
    private lateinit var db: FirebaseFirestore

    private var reportListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_list)

        db = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.rvReports)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        adapter = ReportAdapter()
        recyclerView.adapter = adapter

        loadReports()
    }

    private fun loadReports() {

        val localPending = PendingReportStorage.load(this)

        // اگر قبلاً لیسنر بوده، حذفش کن
        reportListener?.remove()

        reportListener = db.collection("reports")
            .addSnapshotListener { snapshot, error ->

                if (error != null || snapshot == null) {
                    // در صورت خطا، فقط Pending محلی را نشان بده
                    adapter.submitList(localPending)
                    return@addSnapshotListener
                }

                val firestoreReports = snapshot.documents.mapNotNull { doc ->

                    val firestoreReport = doc.toObject(FirestoreReport::class.java)
                        ?: return@mapNotNull null

                    val status = if (doc.metadata.hasPendingWrites()) {
                        ReportStatus.PENDING
                    } else {
                        ReportStatus.SENT
                    }

                    val pendingSource = if (status == ReportStatus.PENDING) {
                        PendingSource.FIRESTORE_QUEUE
                    } else {
                        null
                    }

                    Report(
                        id = firestoreReport.id,
                        errorCode = firestoreReport.errorCode?.toLongOrNull(),
                        visualProblem = firestoreReport.visualProblem,
                        actions = firestoreReport.actions,
                        parts = firestoreReport.parts,
                        timestamp = firestoreReport.timestamp,
                        status = status,
                        pendingSource = pendingSource
                    )
                }


                // IDهایی که الان در Firestore هستند
                val firestoreIds = firestoreReports.map { it.id }.toSet()

                // Pendingهایی که هنوز حتی وارد Firestore هم نشده‌اند
                val realLocalPending = localPending.filter {
                    it.id !in firestoreIds
                }

                // Pendingهایی که Sent شده‌اند را از Storage پاک کن
                val sentIds = firestoreReports
                    .filter { it.status == ReportStatus.SENT }
                    .map { it.id }
                    .toSet()

                PendingReportStorage.save(
                    this,
                    realLocalPending.filter { it.id !in sentIds }
                )

                // لیست نهایی برای UI
                val finalList = mutableListOf<Report>()
                finalList.addAll(realLocalPending)
                finalList.addAll(firestoreReports)

                finalList.sortByDescending { it.timestamp }

                adapter.submitList(finalList)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        reportListener?.remove()
        reportListener = null
    }
}
