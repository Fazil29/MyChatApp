package com.example.chatapp.data.repositories

import android.content.Context
import com.example.chatapp.models.UserModel
import com.example.chatapp.util.helpers.USER_DATA
import com.example.chatapp.util.helpers.readString
import com.example.chatapp.util.helpers.writeString
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LocalRepository @Inject constructor(@ApplicationContext val context: Context) {
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
}