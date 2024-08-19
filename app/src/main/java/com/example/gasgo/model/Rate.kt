package com.example.gasgo.model

import com.google.firebase.firestore.DocumentId

data class Rate (
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val gasStationId: String = "",
    var rate: Int = 0
)