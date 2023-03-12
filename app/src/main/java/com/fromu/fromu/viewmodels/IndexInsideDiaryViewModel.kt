package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.GetMonthListRes
import com.fromu.fromu.data.repository.InsideDiaryRepo
import com.fromu.fromu.ui.base.BaseViewModel
import com.fromu.fromu.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class IndexInsideDiaryViewModel @Inject constructor(private val insideDiaryRepo: InsideDiaryRepo) : BaseViewModel() {

    private var _getMonthListResult: MutableLiveData<Event<Resource<GetMonthListRes>>> = MutableLiveData()
    val getMonthListResult: LiveData<Event<Resource<GetMonthListRes>>>
        get() = _getMonthListResult

    // 일기 월챕터 조회 api
    fun getMonthList(diaryBookId: Int) {
        viewModelScope.launch {
            insideDiaryRepo.getMonthList(diaryBookId).collect {
                _getMonthListResult.value = Event(it)
            }
        }
    }
}