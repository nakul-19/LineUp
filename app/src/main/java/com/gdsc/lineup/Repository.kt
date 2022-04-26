package com.gdsc.lineup

import com.gdsc.lineup.models.ResultHandler
import com.gdsc.lineup.models.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.gdsc.lineup.network.NetworkService
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

    suspend fun registerUser(userModel: UserModel) = flow {
        emit((ResultHandler.Loading))
        registerUserFromNetwork(userModel).collect {
            emit(it)
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun registerUserFromNetwork (userModel: UserModel) = flow {
        kotlin.runCatching {
            emit(ResultHandler.Success(api.registerUser(userModel).body()))
        }.getOrElse { emit(ResultHandler.Failure(it)) }
    }.flowOn(Dispatchers.IO)

}