package com.gdsc.lineup

import android.content.SharedPreferences
import com.gdsc.lineup.login.LoginBody
import com.gdsc.lineup.models.ResultHandler
import com.gdsc.lineup.models.UserModel
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
    private val api: NetworkService,
    private val sp: SharedPreferences
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

    suspend fun loginUser(loginBody: LoginBody) = flow {
        emit((ResultHandler.Loading))
        loginUserFromNetwork(loginBody).collect {
            if (it is ResultHandler.Success){
                emit(it)
                sp.edit().putString("name", it.result?.user?.name).apply()
                sp.edit().putString("email", it.result?.user?.email).apply()
                sp.edit().putString("zealId", it.result?.user?.zealId).apply()
                sp.edit().putString("avatar", it.result?.user?.avatarId).apply()
                sp.edit().putString("teamId", it.result?.user?.teamId).apply()
            }
            else{
                emit(it)
            }
        }
    }

    private suspend fun loginUserFromNetwork(loginBody: LoginBody) = flow {
        kotlin.runCatching {
            emit(ResultHandler.Success(api.login(loginBody).body()))  
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