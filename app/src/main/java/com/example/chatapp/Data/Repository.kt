package com.example.chatapp.Data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.example.chatapp.Models.UserModel
import com.example.chatapp.Util.Helpers.USER_DATA
import com.example.chatapp.Util.Helpers.readString
import com.example.chatapp.Util.Helpers.writeString
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class Repository @Inject constructor(@ApplicationContext val context: Context) {

    lateinit var oneTapClient: SignInClient
    lateinit var signInRequest: BeginSignInRequest

    suspend fun getCredentialsFromLocal(): UserModel? {
        val credentials = context.readString(USER_DATA).first()
        if (credentials.isNotEmpty())
            return Gson().fromJson(credentials, UserModel::class.java)
        return null
    }

    suspend fun saveCredentialsToLocal(userModel: UserModel) {
        val credentials: String = Gson().toJson(userModel)
        context.writeString(USER_DATA, credentials)
    }

    fun prepareClientAndRequest(showSavedAccountsOnly: Boolean) {
        oneTapClient = Identity.getSignInClient(context)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId("671599120358-ksnr7s5ep61l7m9rlhuo927e6bma152n.apps.googleusercontent.com")
                    .setFilterByAuthorizedAccounts(showSavedAccountsOnly)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

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