package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fromu.fromu.data.dto.GetNoticeResult
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.FromCountRes
import com.fromu.fromu.data.remote.network.response.GetNoticeRes
import com.fromu.fromu.data.repository.MainRepo
import com.fromu.fromu.ui.base.BaseViewModel
import com.fromu.fromu.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepo: MainRepo) : BaseViewModel() {
    val fromCountFlow: MutableStateFlow<String> = MutableStateFlow("0")
    val type: MutableStateFlow<Int> = MutableStateFlow(0)
    val noticeInfo: MutableLiveData<ArrayList<GetNoticeResult>> = MutableLiveData()

    // 프롬 개수 조회 결과
    private var _getFromCountResult: MutableLiveData<Event<Resource<FromCountRes>>> = MutableLiveData()
    val getFromCountResult: LiveData<Event<Resource<FromCountRes>>>
        get() = _getFromCountResult


    // 알람 조회 결과
    private var _getNoticeResult: MutableLiveData<Resource<GetNoticeRes>> = MutableLiveData()
    val getNoticeResult: LiveData<Resource<GetNoticeRes>>
        get() = _getNoticeResult


    fun getFromCount() {
        viewModelScope.launch {
            mainRepo.getFromCount().collect {
                _getFromCountResult.value = Event(it)
            }
        }
    }

    /**
     * 알림 조회 api 호출
     */
    fun getNotice() {
        viewModelScope.launch {
            mainRepo.getNotice().collect {
                _getNoticeResult.value = it
            }
        }
    }
}