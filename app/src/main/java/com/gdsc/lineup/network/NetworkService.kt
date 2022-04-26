package project.gdsc.zealicon22.network

import com.gdsc.lineup.leaderBoard.LeaderBoardResponse
import retrofit2.Response
import retrofit2.http.GET


/**
 * Created by Karan verma
 * on 26,April,2022
 */
interface NetworkService {

    @GET("getLeaderboard")
    suspend fun getLeaderBoard() : Response<ArrayList<LeaderBoardResponse>>
}