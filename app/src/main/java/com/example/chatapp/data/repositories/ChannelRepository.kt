package com.example.chatapp.data.repositories

import com.example.chatapp.data.models.ChannelModel
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class ChannelRepository @Inject constructor() {

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
            if(channelObject?.member?.contains(channel.member[1]) == true)
                return it.id
        }
//        if (channels.isNotEmpty())
//            return channels[0].getString("id")
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
                interestedChannels.add(channelObject)
            }
        }
        return interestedChannels
    }
}