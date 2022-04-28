package com.gdsc.lineup.location

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Nakul
 * on 27,April,2022
 */
data class SocketDataModel(
    @Expose
    @SerializedName("teamId")
    val teamId: String?,
    @Expose
    @SerializedName("str")
    val str: MessageModel?
)
