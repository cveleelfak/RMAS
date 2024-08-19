package com.example.gasgo.data

import android.net.Uri
import com.example.gasgo.model.GasStation
import com.example.gasgo.model.service.DatabaseService
import com.example.gasgo.model.service.StorageService
import com.example.gasgo.utils.await
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage


class GasStationRepositoryImpl : GasStationRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val storageInstance = FirebaseStorage.getInstance()

    private val databaseService = DatabaseService(firestoreInstance)
    private val storageService = StorageService(storageInstance)


    override suspend fun getAllGasStations(): Resource<List<GasStation>> {
        return try{
            val snapshot = firestoreInstance.collection("gasStations").get().await()
            val gasStations = snapshot.toObjects(GasStation::class.java)
            Resource.Success(gasStations)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun saveGasStationData(
        description: String,
        crowd: Int,
        mainImage: Uri,
        galleryImages: List<Uri>,
        location: LatLng
    ): Resource<String> {
        return try{
            val currentUser = firebaseAuth.currentUser
            if(currentUser!=null){
                val mainImageUrl = storageService.uploadGasStationMainImage(mainImage)
                val galleryImagesUrls = storageService.uploadGasStationGalleryImages(galleryImages)
                val geoLocation = GeoPoint(
                    location.latitude,
                    location.longitude
                )
                val gasStation = GasStation(
                    userId = currentUser.uid,
                    description = description,
                    crowd = crowd,
                    mainImage = mainImageUrl,
                    galleryImages = galleryImagesUrls,
                    location = geoLocation
                )
                databaseService.saveGasStationData(gasStation)
                databaseService.addPoints(currentUser.uid, 5)
            }
            Resource.Success("Uspesno sačuvani svi podaci o benzinskoj stanici")
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUserGasStations(uid: String): Resource<List<GasStation>> {
        return try {
            val snapshot = firestoreInstance.collection("gasStations")
                .whereEqualTo("userId", uid)
                .get()
                .await()
            val gasStations = snapshot.toObjects(GasStation::class.java)
            Resource.Success(gasStations)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}