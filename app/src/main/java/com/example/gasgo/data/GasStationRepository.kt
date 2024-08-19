package com.example.gasgo.data

import android.net.Uri
import com.example.gasgo.model.GasStation
import com.google.android.gms.maps.model.LatLng

interface GasStationRepository {


        suspend fun getAllGasStations(): Resource<List<GasStation>>
        suspend fun saveGasStationData(
            description: String,
            crowd: Int,
            mainImage: Uri,
            galleryImages: List<Uri>,
            location: LatLng
        ): Resource<String>

        suspend fun getUserGasStations(
            uid: String
        ): Resource<List<GasStation>>

}