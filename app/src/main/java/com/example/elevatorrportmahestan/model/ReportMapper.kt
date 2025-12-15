package com.example.elevatorrportmahestan.model

fun Report.toFirestoreModel(): FirestoreReport {
    return FirestoreReport(
        id = id,
        errorCode = errorCode?.toString(),
        visualProblem = visualProblem,
        actions = actions,
        parts = parts,
        timestamp = timestamp
    )
}

