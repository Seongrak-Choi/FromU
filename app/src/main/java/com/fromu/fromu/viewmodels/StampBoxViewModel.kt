package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.FromCountRes
import com.fromu.fromu.data.remote.network.response.PurchaseStampRes
import com.fromu.fromu.data.remote.network.response.StampCountRes
import com.fromu.fromu.data.repository.StampBoxRepo
import com.fromu.fromu.ui.base.BaseViewModel
import com.fromu.fromu.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StampBoxViewModel @Inject constructor(private val stampBoxRepo: StampBoxRepo) : BaseViewModel() {

    // 우표가 1개 이상 있는지 없는지
    val isPossessStamp: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val fromCountFlow: MutableStateFlow<Int> = MutableStateFlow(0)
    val appbarTitle: MutableStateFlow<String> = MutableStateFlow("우표함")

    private var _getStampListResult: MutableLiveData<Resource<StampCountRes>> = MutableLiveData()
    val getStampListResult: LiveData<Resource<StampCountRes>>
        get() = _getStampListResult


    private var _getFromCountResult: MutableLiveData<Resource<FromCountRes>> = MutableLiveData()
    val getFromCountResult: LiveData<Resource<FromCountRes>>
        get() = _getFromCountResult

    private var _purchaseStampResult: MutableLiveData<Event<Resource<PurchaseStampRes>>> = MutableLiveData()
    val purchaseStampResult: LiveData<Event<Resource<PurchaseStampRes>>>
        get() = _purchaseStampResult


    fun getFromCount() {
        viewModelScope.launch {
            stampBoxRepo.getFromCount().collect {
                _getFromCountResult.value = it
            }
        }
    }

    fun getStampList() {
        viewModelScope.launch {
            stampBoxRepo.getStampList().collect {
                _getStampListResult.value = it
            }
        }
    }

    fun purchaseStamp(stampId: Int) {
        viewModelScope.launch {
            stampBoxRepo.purchaseStamp(stampId).collect {
                _purchaseStampResult.value = Event(it)
            }
        }
    }
}