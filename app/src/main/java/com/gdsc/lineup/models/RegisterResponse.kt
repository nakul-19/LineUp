package com.gdsc.lineup.models

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    val msg: String,
    val token: String,
    val user: User
)
data class User(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val email: String,
    val zealId: String,
    val avatarId: String,
    val teamId : String,
    val score : Int,
    @SerializedName("__v")
    val v : Int
)
