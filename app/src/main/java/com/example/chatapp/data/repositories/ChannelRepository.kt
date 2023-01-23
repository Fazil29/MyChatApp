package com.example.chatapp.data.repositories

import com.example.chatapp.data.models.ChannelModel
import com.example.chatapp.data.models.ChannelType
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class ChannelRepository @Inject constructor(val userRepository: UserRepository) {

    suspend fun getChannel(channel: ChannelModel): String? {
        val db = Firebase.firestore
        val channels =
            db.collection("Channels").whereEqualTo("type", channel.type)
                .whereArrayContains("member", channel.member[0])
                .get(Source.SERVER)
                .await()
                .documents
        channels.forEach {
            val channelObject = it.toObject(ChannelModel::class.java)
            if (channelObject?.member?.contains(channel.member[1]) == true)
                return it.id
        }
        return null
    }

    suspend fun createChannel(channel: ChannelModel): String {
        channel.id = UUID.randomUUID().toString()
        val db = Firebase.firestore
        db.collection("Channels")
            .document(channel.id)
            .set(channel)
            .await()
        return channel.id
    }

    suspend fun getUserInteractedChannels(userId: String): List<ChannelModel> {
        val interestedChannels = mutableListOf<ChannelModel>()
        val db = Firebase.firestore
        val channels =
            db.collection("Channels").whereArrayContains("member", userId).get(Source.SERVER)
                .await().documents
        for (channel in channels) {
            val channelObject = channel.toObject(ChannelModel::class.java)
            if (channelObject != null) {
                mutateUserChannel(channelObject, userId)
                interestedChannels.add(channelObject)
            }
        }
        return interestedChannels
    }

    private suspend fun mutateUserChannel(channelObject: ChannelModel, userId: String) {
        if (channelObject.type == ChannelType.OneToOne) {
            for (otherUserId in channelObject.member) {
                if (otherUserId != userId) {
                    val otherUser = userRepository.getUserFromFireStore(otherUserId)
                    channelObject.profilePic = otherUser!!.profileImage
                    channelObject.name = otherUser.name
                    break
                }
            }
        }
    }
}