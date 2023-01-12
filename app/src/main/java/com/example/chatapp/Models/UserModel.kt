package com.example.chatapp.Models

data class UserModel(
    var id: String? = null,
    var name: String? = null,
    var profileImage: String? = null,
    var email: String? = null,
    var bio: String? = null,
    var gender: Gender? = null,
    var dateOfBirth: String? = null,
)

enum class Gender {
    Male,
    Female,
}
