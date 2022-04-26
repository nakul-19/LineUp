package com.gdsc.lineup.models

data class RegisterResponse(
    val msg: String,
    val token: String,
    val user: User
)
data class User(
    val id: String,
    val name: String,
    val email: String,
    val avatarId: String
)
