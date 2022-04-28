package com.gdsc.lineup

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdsc.lineup.leaderBoard.LeaderBoardResponse
import com.gdsc.lineup.location.*
import com.gdsc.lineup.models.ResultHandler
import com.gdsc.lineup.models.UpdateScoreBody
import com.gdsc.lineup.models.UpdateScoreResponse
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.emitter.Emitter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * @Author: Karan Verma
 * @Date: 26/04/22
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: Repository,
    private val sp: SharedPreferences
) : ViewModel() {

    private val listener = Emitter.Listener {
        Timber.e("mainviewmodelin")
        val data = Gson().fromJson(it.getOrNull(0).toString(), SocketDataModel::class.java)
        if (data == null) {
            Timber.e("Null model received.")
            return@Listener
        } else
            Timber.d(data.toString())
        if (data.str != null && data.teamId == sp.getString("teamId", "123")
            /*&& !sp.getString("teamId", "").isNullOrBlank()*/)
            handleData(data.str)
    }

    init {
        SocketHelper.init()
        SocketHelper.collect(listener)
    }

    private val mLeaders = MutableLiveData<ResultHandler<ArrayList<LeaderBoardResponse>>>()
    val leaders: LiveData<ResultHandler<ArrayList<LeaderBoardResponse>>> = mLeaders

    private val _scanResponse = MutableLiveData<ResultHandler<UpdateScoreResponse>>()
    val scanResponse: LiveData<ResultHandler<UpdateScoreResponse>> = _scanResponse

    fun getLeaderBoard() = viewModelScope.launch {
        mLeaders.postValue(ResultHandler.Loading)
        repo.getLeaderBoard().collect {
            mLeaders.postValue(it as ResultHandler<ArrayList<LeaderBoardResponse>>?)
        }
    }

    fun updateScore(updateScoreBody: UpdateScoreBody) = viewModelScope.launch {
        _scanResponse.postValue(ResultHandler.Loading)
        repo.updateScore(updateScoreBody).collect {
            _scanResponse.postValue(it as ResultHandler<UpdateScoreResponse>)
        }
    }

    private val arrayList = ArrayList<TeammateModel>()

    @Synchronized
    private fun handleData(m: MessageModel) {
        if (arrayList.singleOrNull { it.zealId == m.zealId } == null) {
            arrayList.add(
                TeammateModel(
                    m.zealId,
                    m.avatar,
                    m.longitude,
                    m.latitude,
                    calculateDistance(m.latitude, m.longitude)
                )
            )
        } else {
            val old = arrayList.singleOrNull { it.zealId == m.zealId }!!
            arrayList.remove(old)
            val new = old.copy(distance = calculateDistance(m.latitude, m.longitude))
            arrayList.add(new)
        }
        updateTeammates()
    }

    private val mFirst = MutableLiveData<TeammateModel?>(null)
    val first: LiveData<TeammateModel?> = mFirst

    private val mSecond = MutableLiveData<TeammateModel?>(null)
    val second: LiveData<TeammateModel?> = mSecond

    private val mThird = MutableLiveData<TeammateModel?>(null)
    val third: LiveData<TeammateModel?> = mThird

    private fun updateTeammates() {
        arrayList.sortBy { it.distance }
        mFirst.postValue(arrayList.getOrNull(0))
        mSecond.postValue(arrayList.getOrNull(1))
        mThird.postValue(arrayList.getOrNull(2))
    }

    private fun calculateDistance(lat1: Double, lon1: Double): Double {
        val lat2 = (LocationService.lastLocation?.latitude ?: 0).toDouble()
        val lon2 = (LocationService.lastLocation?.longitude ?: 0).toDouble()
        val r = 6371
        val dLat = deg2rad(lat2 - lat1)
        val dLon = deg2rad(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(deg2rad(lat1)) * cos(deg2rad(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c * 1000
    }

    private fun deg2rad(deg: Double): Double {
        return deg * (Math.PI / 180)
    }

    override fun onCleared() {
        SocketHelper.stopCollection(listener)
        super.onCleared()
    }

    fun resetScanResult() {
        _scanResponse.postValue(ResultHandler.Loading)
    }

    fun getUserId() = repo.getUserId()

}
