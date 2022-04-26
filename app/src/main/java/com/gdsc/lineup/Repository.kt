package com.gdsc.lineup

import com.gdsc.lineup.models.ResultHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.gdsc.lineup.network.NetworkService
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * @Author: Karan Verma
 * @Date: 26/04/22
 */

class Repository @Inject constructor(
    private val api: NetworkService
) {

    suspend fun getLeaderBoard() = flow {
        emit(ResultHandler.Loading)
        fetchDataFromNetwork().collect {
            if (it is ResultHandler.Success){
                emit(it)
            }else emit(it)
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun fetchDataFromNetwork() = flow {
        runCatching {
            emit(ResultHandler.Success(api.getLeaderBoard().body()))
        }.getOrElse { emit(ResultHandler.Failure(it)) }
    }.flowOn(Dispatchers.IO)

    suspend fun updateScore(userIdOne: String, userIdTwo: String) = flow {
        runCatching {
            val data = JsonObject()
            data.addProperty("userId1", userIdOne)
            data.addProperty("userId2", userIdTwo)
            emit(ResultHandler.Success(api.updateScore(data).body()))
        }.getOrElse { emit(ResultHandler.Failure(it)) }
    }.flowOn(Dispatchers.IO)

}