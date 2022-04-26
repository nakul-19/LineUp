package com.gdsc.lineup

import com.gdsc.lineup.models.ResultHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import project.gdsc.zealicon22.network.NetworkService
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

}