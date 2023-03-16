package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.FromCountRes
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

    // 프롬 개수 조회 결과
    private var _getFromCountResult: MutableLiveData<Event<Resource<FromCountRes>>> = MutableLiveData()
    val getFromCountResult: LiveData<Event<Resource<FromCountRes>>>
        get() = _getFromCountResult

    fun getFromCount() {
        viewModelScope.launch {
            mainRepo.getFromCount().collect {
                _getFromCountResult.value = Event(it)
            }
        }
    }
}