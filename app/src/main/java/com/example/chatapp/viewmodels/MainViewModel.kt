package com.example.chatapp.viewmodels

import android.app.Activity
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.repositories.LocalRepository
import com.example.chatapp.data.repositories.StorageRepository
import com.example.chatapp.data.repositories.UserRepository
import com.example.chatapp.models.Gender
import com.example.chatapp.models.UserModel
import com.example.chatapp.util.Response
import com.example.chatapp.util.Screen
import com.example.chatapp.util.helpers.SignInHelper
import com.example.chatapp.util.isValidUrl
import com.example.chatapp.util.toBitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val userRepository: UserRepository,
    val localRepository: LocalRepository,
    val storageRepository: StorageRepository
) : ViewModel() {

    var user: UserModel = UserModel( "", "", "", "", "", Gender.Male, "")

    private val _credentialsFromLocal = mutableStateOf<Response<UserModel?>>(Response.LOADING())
    val credentialsFromLocal = _credentialsFromLocal

    fun getCredentialsFromLocal() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val credentials = localRepository.getCredentialsFromLocal()
                credentials?.let {
                    user = it
                    withContext(Dispatchers.Main) {
                        _credentialsFromLocal.value = Response.SUCCESS(credentials)
                    }
                } ?: run {
                    _credentialsFromLocal.value = Response.ERROR("Credentials not found in local")
                }
            }
        } catch (e: Exception) {
            _credentialsFromLocal.value = Response.ERROR("$e")
        }
    }

    suspend fun saveCredentials(
        id: String,
        name: String,
        profileImage: String,
        email: String,
        bio: String,
        dob: String,
        isMale: Boolean
    ) {
        val gender: Gender = if (isMale) Gender.Male else Gender.Female
        var remotePicUri = profileImage
        if (!remotePicUri.isValidUrl()) {
            val profilePicBitmap = Uri.parse(profileImage).toBitmap(localRepository.context)
            remotePicUri = storageRepository.saveProfilePicToFirebase(profilePicBitmap, id)
        }
        val res = UserModel(id, name, remotePicUri, email, bio, gender, dob)
        userRepository.saveUserToFireStore(res)
        localRepository.saveCredentialsToLocal(res)
        user = res
    }

    fun openDialog(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        SignInHelper.prepareClientAndRequest(localRepository.context, false)
        viewModelScope.launch {
            val result = SignInHelper.oneTapClient.beginSignIn(SignInHelper.signInRequest).await()
            val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
            launcher.launch(intentSenderRequest)
        }
    }

    suspend fun signIn(result: ActivityResult, navigate: (String) -> Unit) {
        if (result.resultCode == Activity.RESULT_OK) {
            val credentials = SignInHelper.oneTapClient.getSignInCredentialFromIntent(result.data)
            credentials.googleIdToken?.let {
                val firestoreUser = userRepository.getUserFromFireStore(credentials.id)

                if (firestoreUser != null) {
                    localRepository.saveCredentialsToLocal(firestoreUser)
                    user = firestoreUser
                    navigate(Screen.Home.route)
                } else {
                    credentials.apply {
                        user = UserModel(
                            id = id,
                            name = "$givenName $familyName",
                            profileImage = "$profilePictureUri",
                            email = id,
                            bio = "",
                            gender = Gender.Male,
                            dateOfBirth = ""
                        )
                        navigate(Screen.EditProfile.route)
                    }
                }
            }
        }
    }
}