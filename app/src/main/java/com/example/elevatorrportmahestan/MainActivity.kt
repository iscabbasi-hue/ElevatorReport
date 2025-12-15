package com.example.elevatorrportmahestan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.elevatorrportmahestan.model.PendingSource
import com.example.elevatorrportmahestan.model.Report
import com.example.elevatorrportmahestan.model.ReportStatus
import com.example.elevatorrportmahestan.storage.PendingReportStorage
import com.example.elevatorrportmahestan.data.worker.enqueueReportSync
import java.util.UUID
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var etErrorCode: EditText
    private lateinit var etVisualProblem: EditText
    private lateinit var etActions: EditText
    private lateinit var etParts: EditText
    private lateinit var btnSend: Button

    override fun onBackPressed() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseAuth.getInstance()

        setContentView(R.layout.activity_main)

        etErrorCode = findViewById(R.id.etErrorCode)
        etVisualProblem = findViewById(R.id.etVisualProblem)
        etActions = findViewById(R.id.etActions)
        etParts = findViewById(R.id.etParts)
        btnSend = findViewById(R.id.btnSend)

        btnSend.setOnClickListener {
            saveReportAndSync()
        }

        findViewById<Button>(R.id.btnShowReports).setOnClickListener {
            startActivity(Intent(this, ReportListActivity::class.java))
        }
    }

    private fun saveReportAndSync() {

        val errorCodeText = etErrorCode.text.toString().trim()
        val visualProblem = etVisualProblem.text.toString().trim()
        val actions = etActions.text.toString().trim()
        val parts = etParts.text.toString().trim()

        if (errorCodeText.isEmpty() || visualProblem.isEmpty()) {
            Toast.makeText(this, "فیلدهای ضروری را پر کنید", Toast.LENGTH_SHORT).show()
            return
        }

        val errorCode = errorCodeText.toLongOrNull()
        if (errorCode == null) {
            Toast.makeText(this, "کد خطا باید عددی باشد", Toast.LENGTH_SHORT).show()
            return
        }

        val report = Report(
            id = UUID.randomUUID().toString(),

            errorCode = errorCode,
            visualProblem = visualProblem,
            actions = if (actions.isEmpty()) null else actions,
            parts = if (parts.isEmpty()) null else parts,

            timestamp = System.currentTimeMillis(),
            status = ReportStatus.PENDING,
            pendingSource = PendingSource.LOCAL_ONLY
        )




        val pendingList = PendingReportStorage.load(this)
        pendingList.add(report)
        PendingReportStorage.save(this, pendingList)

        enqueueReportSync(this, report.id)

        Toast.makeText(this, "گزارش ذخیره شد", Toast.LENGTH_SHORT).show()

        startActivity(Intent(this, ReportListActivity::class.java))
        finish()
    }


}
