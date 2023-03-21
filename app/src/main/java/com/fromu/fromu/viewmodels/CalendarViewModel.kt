package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.GetSchedulesListRes
import com.fromu.fromu.data.repository.CalendarRepo
import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(private val calendarRepo: CalendarRepo) : BaseViewModel() {

    // 해당 월의 일정이 있는 날짜 조회 결과
    private var _getSchedulesListResult: MutableLiveData<Resource<GetSchedulesListRes>> = MutableLiveData()
    val getSchedulesListResult: LiveData<Resource<GetSchedulesListRes>>
        get() = _getSchedulesListResult


    /**
     * 특정 달의 일정이 있는 요일을 조회
     *
     * @param month
     */
    fun getScheduleList(month: String) {
        viewModelScope.launch {
            calendarRepo.getSchedulesList(month).collect {
                _getSchedulesListResult.value = it
            }
        }
    }
}