package com.example.chatapp.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.repositories.ChannelRepository
import com.example.chatapp.data.repositories.UserRepository
import com.example.chatapp.data.models.ChannelModel
import com.example.chatapp.data.models.ChannelType
import com.example.chatapp.data.models.UserModel
import com.example.chatapp.data.repositories.StorageRepository
import com.example.chatapp.util.Response
import com.example.chatapp.util.toBitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val userRepository: UserRepository,
    val channelRepository: ChannelRepository,
    val storageRepository: StorageRepository,
) : ViewModel() {

    private val _users = mutableStateOf<Response<List<UserModel>>>(Response.LOADING())
    val users = _users

    private val _ourInteractedChannels =
        mutableStateOf<Response<List<ChannelModel>>>(Response.LOADING())
    val ourInteractedChannels = _ourInteractedChannels

    var viewingUserModel = UserModel()

    fun setViewingUser(userModel: UserModel) {
        viewingUserModel = userModel
    }

    fun getUsers(ourUserId: String) {
        _users.value = Response.LOADING()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userList = userRepository.getUsersFromFirebase(ourUserId)
                if (userList.isEmpty())
                    _users.value = Response.ERROR("No User Found!")
                else
                    _users.value = Response.SUCCESS(userList)
            } catch (e: Exception) {
                _users.value = Response.ERROR("Something Went Wrong!")
            }
        }
    }

    suspend fun getChannelId(users: List<UserModel>): String {
        val sender = users[0]
        val receiver = users[1]
        val members = mutableListOf<String>()
        users.forEach { members.add(it.id) }
        val channel = ChannelModel(
//            profilePic = receiver.profileImage,
            type = ChannelType.OneToOne,
            name = "${receiver.name}-${sender.name}",
            member = members
        )

        val id = channelRepository.getChannel(channel)
        if (id != null)
            return id
        return channelRepository.createChannel(channel)
    }

    suspend fun createGroupChannel(channel: ChannelModel): String {
        channel.type = ChannelType.Group
        channel.profilePic = getPicRemoteUrl(channel.profilePic, UUID.randomUUID().toString())
        return channelRepository.createChannel(channel)
    }

    private suspend fun getPicRemoteUrl(localUri: String, id: String): String {
        val bitmap = localUri.toUri().toBitmap(context)
        return storageRepository.saveProfilePicToFirebase(bitmap, id)
    }

    fun getOurInteractedChannels(ourUserId: String) {
        _ourInteractedChannels.value = Response.LOADING()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val channels = channelRepository.getUserInteractedChannels(ourUserId)
                if (channels.isEmpty())
                    _ourInteractedChannels.value = Response.ERROR("No Channels Found")
                else {
                    _ourInteractedChannels.value = Response.SUCCESS(channels)
                }
            } catch (e: Exception) {
                _ourInteractedChannels.value = Response.ERROR("Something went wrong")
            }
        }
    }
}