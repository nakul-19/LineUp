package com.gdsc.lineup.location

import android.location.Location
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Nakul
 * on 26,April,2022
 */
data class MessageModel(
    @Expose
    @SerializedName("zealId")
    val zealId: String,
    @Expose
    @SerializedName("avatar")
    val avatar: String,
    @Expose
    @SerializedName("longitude")
    val longitude: Double,
    @Expose
    @SerializedName("latitude")
    val latitude: Double
)