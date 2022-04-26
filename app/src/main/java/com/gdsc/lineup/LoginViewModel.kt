package com.gdsc.lineup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdsc.lineup.models.RegisterResponse
import com.gdsc.lineup.models.ResultHandler
import com.gdsc.lineup.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Author: Anuraj Jain
 * @Date: 26/04/22
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

    private val mRegisterRes = MutableLiveData<ResultHandler<RegisterResponse>>()
    val registerRes: LiveData<ResultHandler<RegisterResponse>>
    get() = mRegisterRes

    fun registerUser(userModel: UserModel) = viewModelScope.launch {
        mRegisterRes.postValue(ResultHandler.Loading)
        repo.registerUser(userModel).collect {
            mRegisterRes.postValue(it as ResultHandler<RegisterResponse>)
        }
    }

}