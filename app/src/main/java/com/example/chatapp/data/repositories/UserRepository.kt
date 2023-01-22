package com.example.chatapp.data.repositories

import com.example.chatapp.data.models.UserModel
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor() {

    suspend fun getUserFromFireStore(userId: String): UserModel? {
        val db = Firebase.firestore
        val fireStoreRes = db.collection("Users")
            .document(userId)
            .get(Source.SERVER)
            .await()
        val user = fireStoreRes.toObject(UserModel::class.java)
        return user
    }

    suspend fun saveUserToFireStore(user: UserModel) {
        val db = Firebase.firestore
        db.collection("Users")
            .document(user.id)
            .set(user)
            .await()
    }

    suspend fun getUsersFromFirebase(ourUserId: String): List<UserModel> {
        val userList = mutableListOf<UserModel>()
        val db = Firebase.firestore
        val users = db.collection("Users").whereNotEqualTo("id", ourUserId).get(Source.SERVER).await().documents
        for (user in users) {
            val userObject = user.toObject(UserModel::class.java)
            if (userObject != null) {
                userList.add(userObject)
            }
        }
        return userList
    }
}