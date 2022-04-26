package com.gdsc.lineup.network

import com.gdsc.lineup.leaderBoard.LeaderBoardResponse
import com.gdsc.lineup.models.RegisterResponse
import com.gdsc.lineup.models.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


/**
 * Created by Karan verma
 * on 26,April,2022
 */
interface NetworkService {

    @GET("getLeaderboard")
    suspend fun getLeaderBoard() : Response<ArrayList<LeaderBoardResponse>>

    @POST("register")
    suspend fun registerUser(@Body userModel: UserModel): Response<RegisterResponse>
}