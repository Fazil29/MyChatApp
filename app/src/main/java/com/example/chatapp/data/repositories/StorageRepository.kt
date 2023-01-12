package com.example.chatapp.data.repositories

import android.graphics.Bitmap
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class StorageRepository @Inject constructor() {

    suspend fun saveProfilePicToFirebase(bitmap: Bitmap, id: String): String {
        val imageRef = FirebaseStorage.getInstance().reference.child(id)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()
        imageRef.putBytes(data).await()
        val imageLink = imageRef.downloadUrl.await()
        return "$imageLink"
    }

}