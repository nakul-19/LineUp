package com.gdsc.lineup.models

data class UserModel(
    val name: String?,
    val email: String?,
    val zealId: String,
    val password: String,
    var avatarId: String
)
