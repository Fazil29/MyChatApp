package com.example.chatapp.models

data class UserModel(
    var id: String = "",
    var name: String = "",
    var profileImage: String = "",
    var email: String = "",
    var bio: String = "",
    var gender: Gender = Gender.Male,
    var dateOfBirth: String = "",
)

enum class Gender {
    Male,
    Female,
}
