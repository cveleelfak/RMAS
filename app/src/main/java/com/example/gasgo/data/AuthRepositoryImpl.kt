package com.example.gasgo.data

import android.net.Uri
import com.example.gasgo.model.CustomUser
import com.example.gasgo.model.service.DatabaseService
import com.example.gasgo.model.service.StorageService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class AuthRepositoryImp : AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val storageInstance = FirebaseStorage.getInstance()

    private val databaseService = DatabaseService(firestoreInstance)
    private val storageService = StorageService(storageInstance)
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        }catch(e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun register(
        fullName: String,
        phoneNumber: String,
        profileImage: Uri,
        email: String,
        password: String
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            if(result.user != null){
                val profilePictureUrl = storageService.uploadProfilePicture(result.user!!.uid, profileImage)
                val user = CustomUser(
                    fullName = fullName,
                    phoneNumber = phoneNumber,
                    profileImage = profilePictureUrl
                )
                databaseService.saveUserData(result.user!!.uid, user)
            }
            Resource.Success(result.user!!)
        }catch(e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUserData(): Resource<CustomUser> {
        return try {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                val uid = currentUser.uid

                val db = FirebaseFirestore.getInstance()

                val userDocRef = db.collection("users").document(uid)
                val userSnapshot = userDocRef.get().await()

                if (userSnapshot.exists()) {
                    val customUser = userSnapshot.toObject(CustomUser::class.java)
                    if (customUser != null) {
                        Resource.Success(customUser)
                    } else {
                        Resource.Failure(Exception("Mapiranje dokumenta na CustomUser nije uspelo"))
                    }
                } else {
                    Resource.Failure(Exception("Korisnikov dokument ne postoji"))
                }
            } else {
                Resource.Failure(Exception("Nema trenutnog korisnika"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getAllUserData(): Resource<List<CustomUser>> {
        return try {
            val db = FirebaseFirestore.getInstance()
            val usersCollectionRef = db.collection("users")
            val usersSnapshot = usersCollectionRef.get().await()

            if (!usersSnapshot.isEmpty) {
                val usersList = usersSnapshot.documents.mapNotNull { document ->
                    document.toObject(CustomUser::class.java)
                }
                Resource.Success(usersList)
            } else {
                Resource.Failure(Exception("Nema korisnika u bazi podataka"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}