package com.gdsc.lineup

import android.content.SharedPreferences
import com.gdsc.lineup.login.LoginBody
import com.gdsc.lineup.models.ResultHandler
import com.gdsc.lineup.models.UpdateScoreBody
import com.gdsc.lineup.models.UserModel
import com.google.gson.JsonObject
import com.gdsc.lineup.network.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
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
            if (it is ResultHandler.Success) {
                emit(it)
            } else emit(it)
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
            val result = api.registerUser(userModel)
            if (result.isSuccessful)
                emit(ResultHandler.Success(result.body()))
            else {
                throw Throwable(getBackendMessage(result.errorBody()))
            }
        }.getOrElse { emit(ResultHandler.Failure(it)) }
    }.flowOn(Dispatchers.IO)

    suspend fun loginUser(loginBody: LoginBody) = flow {
        emit((ResultHandler.Loading))
        loginUserFromNetwork(loginBody).collect {
            Timber.d("Result1 $it")
            if (it is ResultHandler.Success) {
                val body = it.result
                emit(it)
                sp.edit().putString("name", body?.user?.name).apply()
                sp.edit().putString("userId", body?.user?.id).apply()
                sp.edit().putString("id", body?.user?.id).apply()
                sp.edit().putString("email", body?.user?.email).apply()
                sp.edit().putString("zealId", body?.user?.zealId).apply()
                sp.edit().putString("avatar", body?.user?.avatarId).apply()
                sp.edit().putString("teamId", body?.user?.teamId).apply()
            }
            else{
                emit(it)
            }
        }
    }

    fun getUserId(): String {
        return sp.getString("userId", "1")!!
    }

    private suspend fun loginUserFromNetwork(loginBody: LoginBody) = flow {
        kotlin.runCatching {
            val result = api.login(loginBody)
            if (result.isSuccessful)
                emit(ResultHandler.Success(result.body()))
            else {
                throw Throwable(getBackendMessage(result.errorBody()))
            }
        }.getOrElse { emit(ResultHandler.Failure(it)) }
    }.flowOn(Dispatchers.IO)

    suspend fun updateScore(updateScoreBody: UpdateScoreBody) = flow {
        runCatching {
            val result = api.updateScore(updateScoreBody)
            if(result.isSuccessful)
                emit(ResultHandler.Success(result.body()))
            else
                throw Throwable(getBackendMessage(result.errorBody()))
        }.getOrElse { emit(ResultHandler.Failure(it)) }
    }.flowOn(Dispatchers.IO)

    private fun getBackendMessage(errorBody: ResponseBody?): String {

        var msg = ""
        val error = errorBody?.string()

        error?.let {
            msg = try {
                JSONObject(it).getString("msg")
            } catch (e: JSONException) {
                "Something went wrong!"
            }
        }

        return msg
    }

}