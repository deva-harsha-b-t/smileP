package com.dtl.smilep

import android.app.Application
import com.google.firebase.FirebaseApp


class LCOfaceDetection: Application(){
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
    companion object{
        const val RESULT_TEXT = "RESULT_TEXT"
        const val RESULT_DIALOG = "RESULT_DIALOG"
    }
}