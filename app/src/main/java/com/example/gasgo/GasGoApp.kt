package com.example.gasgo

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore



class GasGoApp : Application() {
    val db by lazy { Firebase.firestore}
}