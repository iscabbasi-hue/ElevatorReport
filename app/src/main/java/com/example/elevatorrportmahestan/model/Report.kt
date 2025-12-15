package com.example.elevatorrportmahestan.model

enum class ReportStatus {
    PENDING,
    SENT
}




//data class Report(
//    val id: String,
//  val title: String,
    //val description: String,
    //val timestamp: Long,
    //val status: ReportStatus,
    //val pendingSource: PendingSource
//)

//data class Report(
//    val id: String,
//    val title: String,
//    val description: String,
//    val timestamp: Long,
//    val status: ReportStatus,
//    val pendingSource: PendingSource?   // ✅ nullable
//)

data class Report(
    val id: String,

    // ✅ داده‌های اصلی گزارش
    val errorCode: Long?,
    val visualProblem: String?,
    val actions: String?,
    val parts: String?,

    // ✅ زمان
    val timestamp: Long,

    // ✅ وضعیت ارسال
    val status: ReportStatus,

    // ✅ فقط وقتی Pending است مقدار دارد
    val pendingSource: PendingSource?
)