package com.example.chatapp.data.models

data class ChannelModel(
    var id: String = "",
    var profilePic: String = "",
    var type: ChannelType = ChannelType.OneToOne,
    var name: String = "",
    var description: String = "",
    var member: List<String> = listOf(),
    var messages: List<MessageModel> = listOf()
)

enum class ChannelType {
    OneToOne,
    Group,
}