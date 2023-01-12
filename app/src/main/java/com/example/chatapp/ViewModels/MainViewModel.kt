package com.example.chatapp.ViewModels

import android.app.Activity
import android.graphics.Bitmap
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.Data.Repository
import com.example.chatapp.Models.UserModel
import com.example.chatapp.Util.Response
import com.example.chatapp.Util.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    var user: UserModel = UserModel()

    private val _credentialsFromLocal = mutableStateOf<Response<UserModel?>>(Response.LOADING())
    val credentialsFromLocal = _credentialsFromLocal

    fun getCredentialsFromLocal() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val credentials = repository.getCredentialsFromLocal()
                credentials?.let { user = it }
                withContext(Dispatchers.Main) {
                    _credentialsFromLocal.value = Response.SUCCESS(credentials)
                }
            }
        } catch (e: Exception) {
            _credentialsFromLocal.value = Response.ERROR("$e")
        }
    }

    fun saveCredentials(userModel: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveUserToFireStore(userModel)
            repository.saveCredentialsToLocal(userModel)
            user = userModel
        }
    }

    fun openDialog(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        repository.prepareClientAndRequest(false)
        viewModelScope.launch {
            val result = repository.oneTapClient.beginSignIn(repository.signInRequest).await()
            val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
            launcher.launch(intentSenderRequest)
        }
    }

    suspend fun signIn(result: ActivityResult, navigate: (String) -> Unit) {
        if (result.resultCode == Activity.RESULT_OK) {
            val credentials = repository.oneTapClient.getSignInCredentialFromIntent(result.data)
            credentials.googleIdToken?.let {
                val firestoreUser = repository.getUserFromFireStore(credentials.id)

                if (firestoreUser != null) {
                    repository.saveCredentialsToLocal(firestoreUser)
                    user = firestoreUser
                    navigate(Screen.Home.route)
                } else {
                    credentials.apply {
                        user = UserModel(
                            id = id,
                            name = "$givenName $familyName",
                            profileImage = "$profilePictureUri",
                            email = id
                        )
                        navigate(Screen.EditProfile.route)
                    }
                }
            }
        }
    }

    suspend fun saveProfilePicToFirebase(bitmap: Bitmap, id: String): String{
        return repository.saveProfilePicToFirebase(bitmap, id)
    }
}