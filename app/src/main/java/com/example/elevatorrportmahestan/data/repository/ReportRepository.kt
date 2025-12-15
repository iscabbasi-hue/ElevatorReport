package com.example.elevatorrportmahestan.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

object ReportRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private var globalListener: ListenerRegistration? = null

    fun startGlobalListener() {
        if (globalListener != null) return

        globalListener = firestore
            .collection("reports")
            .addSnapshotListener { _, error ->
                if (error != null) {
                    error.printStackTrace()
                }
            }
    }
}
