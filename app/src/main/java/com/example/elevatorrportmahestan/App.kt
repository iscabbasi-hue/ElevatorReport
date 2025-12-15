package com.example.elevatorrportmahestan

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.example.elevatorrportmahestan.data.repository.ReportRepository


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val firestore = FirebaseFirestore.getInstance()
        firestore.enableNetwork()

        ReportRepository.startGlobalListener()
    }
}
