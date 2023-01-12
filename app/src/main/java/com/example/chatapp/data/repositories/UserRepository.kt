package com.example.chatapp.data.repositories

import com.example.chatapp.models.UserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor() {

    suspend fun getUserFromFireStore(userId: String): UserModel? {
        val db = Firebase.firestore
        val fireStoreRes = db.collection("Users")
            .document(userId)
            .get()
            .await()

        return fireStoreRes.toObject(UserModel::class.java)
    }

    suspend fun saveUserToFireStore(user: UserModel) {
        val db = Firebase.firestore
        db.collection("Users")
            .document(user.id!!)
            .set(user)
            .await()
    }
}