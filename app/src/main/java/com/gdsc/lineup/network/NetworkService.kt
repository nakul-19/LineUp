package com.gdsc.lineup.network

import com.gdsc.lineup.leaderBoard.LeaderBoardResponse
<<<<<<< Updated upstream
=======
import com.gdsc.lineup.login.LoginBody
import com.gdsc.lineup.models.RegisterResponse
import com.gdsc.lineup.models.UpdateScoreBody
import com.gdsc.lineup.models.UserModel
>>>>>>> Stashed changes
import com.gdsc.lineup.models.UpdateScoreResponse
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by Karan verma
 * on 26,April,2022
 */
interface NetworkService {

    @GET("getLeaderboard")
    suspend fun getLeaderBoard() : Response<ArrayList<LeaderBoardResponse>>

    @POST("updateScore")
<<<<<<< Updated upstream
    suspend fun updateScore(data: JsonObject): Response<UpdateScoreResponse>

=======
    suspend fun updateScore(@Body updateScoreBody: UpdateScoreBody): Response<UpdateScoreResponse>
>>>>>>> Stashed changes
}