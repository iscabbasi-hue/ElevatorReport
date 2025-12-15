


package com.example.elevatorrportmahestan.model

data class FirestoreReport(
    val id: String = "",
    val errorCode: String? = null,
    val visualProblem: String? = null,
    val actions: String? = null,
    val parts: String? = null,
    val timestamp: Long = 0L
)

