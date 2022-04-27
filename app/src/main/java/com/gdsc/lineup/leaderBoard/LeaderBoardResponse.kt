package com.gdsc.lineup.leaderBoard

import com.google.gson.annotations.SerializedName


data class LeaderBoardResponse(
    @SerializedName("__v")
    val v: Int,
    @SerializedName("_id")
    val id: String,
    val name: String,
    val score: Int,
    val userId: String,
    val zealId: String,
    val avatarId: String,
)