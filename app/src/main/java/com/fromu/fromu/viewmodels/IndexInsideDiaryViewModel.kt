package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.IndexByMonthRes
import com.fromu.fromu.data.remote.network.response.IndexMonthListRes
import com.fromu.fromu.data.repository.InsideDiaryRepo
import com.fromu.fromu.ui.base.BaseViewModel
import com.fromu.fromu.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class IndexInsideDiaryViewModel @Inject constructor(private val insideDiaryRepo: InsideDiaryRepo) : BaseViewModel() {


    //  일기 월 챕터 조회 api
    private var _indexMonthListResult: MutableLiveData<Event<Resource<IndexMonthListRes>>> = MutableLiveData()
    val indexMonthListResult: LiveData<Event<Resource<IndexMonthListRes>>>
        get() = _indexMonthListResult


    //  일기 월별 조회 api
    private var _indexByMonthListResult: MutableLiveData<Event<Resource<IndexByMonthRes>>> = MutableLiveData()
    val indexByMonthListResult: LiveData<Event<Resource<IndexByMonthRes>>>
        get() = _indexByMonthListResult


    // 일기 월챕터 조회 api
    fun getMonthList(diaryBookId: Int) {
        viewModelScope.launch {
            insideDiaryRepo.getMonthList(diaryBookId).collect {
                _indexMonthListResult.value = Event(it)
            }
        }
    }

    // 일기 월별 일기 조회 api
    fun getDiariesByMonth(diaryBookId: Int, month: String) {
        viewModelScope.launch {
            insideDiaryRepo.getDiariesByMonth(diaryBookId, month).collect {
                _indexByMonthListResult.value = Event(it)
            }
        }
    }
}