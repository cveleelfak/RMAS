package com.example.gasgo.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.GeoPoint



data class GasStation(
    @DocumentId val id: String = "",
    val userId: String = "",
    val description: String = "",
    val crowd: Int = 0,
    val mainImage: String = "",
    val galleryImages: List<String> = emptyList(),
    val location: GeoPoint = GeoPoint(0.0, 0.0)
)
