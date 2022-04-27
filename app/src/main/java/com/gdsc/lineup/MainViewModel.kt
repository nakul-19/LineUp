package com.gdsc.lineup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.gdsc.lineup.leaderBoard.LeaderBoardResponse
import com.gdsc.lineup.models.ResultHandler
import com.gdsc.lineup.models.UpdateScoreResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * @Author: Karan Verma
 * @Date: 26/04/22
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

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

    fun updateScore(userIdOne: String, userIdTwo: String) = viewModelScope.launch {
        _scanResponse.postValue(ResultHandler.Loading)
        repo.updateScore(userIdOne, userIdTwo).collect {
            _scanResponse.postValue(it as ResultHandler<UpdateScoreResponse>)
        }
    }

}
