package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fromu.fromu.data.dto.CoupleRes
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.SetBellMsgReq
import com.fromu.fromu.data.remote.network.response.*
import com.fromu.fromu.data.repository.MyHomeRepo
import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyHomeViewModel @Inject constructor(private val myHomeRepo: MyHomeRepo) : BaseViewModel() {

    val couple: MutableLiveData<CoupleRes> = MutableLiveData()
    val dday: MutableStateFlow<String> = MutableStateFlow("00")
    val currentBellMsg: MutableStateFlow<String> = MutableStateFlow("")

    private var _coupleInfoResult: MutableLiveData<Resource<CheckMatchingRes>> = MutableLiveData()
    val coupleInfoResult: LiveData<Resource<CheckMatchingRes>>
        get() = _coupleInfoResult


    private var _logoutResult: MutableLiveData<Resource<LogoutRes>> = MutableLiveData()
    val logoutResult: LiveData<Resource<LogoutRes>>
        get() = _logoutResult


    private var _breakMatchingResult: MutableLiveData<Resource<BreakMatchingRes>> = MutableLiveData()
    val breakMatchingResult: LiveData<Resource<BreakMatchingRes>>
        get() = _breakMatchingResult


    private var _withdrawalResult: MutableLiveData<Resource<WithdrawalRes>> = MutableLiveData()
    val withdrawalResult: LiveData<Resource<WithdrawalRes>>
        get() = _withdrawalResult

    private var _setBellMsgResult: MutableLiveData<Resource<SetBellMsgRes>> = MutableLiveData()
    val setBellMsgResult: LiveData<Resource<SetBellMsgRes>>
        get() = _setBellMsgResult

    private var _getBellMsgResult: MutableLiveData<Resource<GetBellMsgRes>> = MutableLiveData()
    val getBellMsgResult: LiveData<Resource<GetBellMsgRes>>
        get() = _getBellMsgResult


    fun getCoupleInfo() {
        viewModelScope.launch {
            myHomeRepo.getCoupleInfo().collect {
                _coupleInfoResult.value = it
            }
        }
    }

    /**
     * 로그아웃 api
     */
    fun logout() {
        viewModelScope.launch {
            myHomeRepo.logout().collect {
                _logoutResult.value = it
            }
        }
    }


    /**
     * 커플 연결 끊기 api
     */
    fun breakMatching() {
        viewModelScope.launch {
            myHomeRepo.breakMatching().collect {
                _breakMatchingResult.value = it
            }
        }
    }

    fun withdrawal() {
        viewModelScope.launch {
            myHomeRepo.withdrawal().collect {
                _withdrawalResult.value = it
            }
        }
    }

    /**
     * 벨 울리기 멘트 변경
     */
    fun setBellMsg(setBellMsgReq: SetBellMsgReq) {
        viewModelScope.launch {
            myHomeRepo.setBellMsg(setBellMsgReq).collect {
                _setBellMsgResult.value = it
            }
        }
    }

    /**
     * 설정 되어 있는 벨 멘트 조회
     */
    fun getBellMsg() {
        viewModelScope.launch {
            myHomeRepo.getBellMsg().collect {
                _getBellMsgResult.value = it
            }
        }
    }
}