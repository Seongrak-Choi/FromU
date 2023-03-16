package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.StampCountRes
import com.fromu.fromu.data.repository.WriteLetterRepo
import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectStampViewModel @Inject constructor(private val writeLetterRepo: WriteLetterRepo) : BaseViewModel() {
    // 선택한 우표 id
    val selectStampId: MutableStateFlow<Int?> = MutableStateFlow(null)

    // 우표를 소지하고 있는 지
    val isPossessStamp: MutableStateFlow<Boolean> = MutableStateFlow(false)


    // getStampList 결과 저장
    private var _getStampCountResult: MutableLiveData<Resource<StampCountRes>> = MutableLiveData()
    val getStampCountResult: LiveData<Resource<StampCountRes>>
        get() = _getStampCountResult


    /**
     * 유저가 소유한 우표를 조회
     */
    fun getStampList() {
        viewModelScope.launch {
            writeLetterRepo.getStampList().collect {
                _getStampCountResult.value = it
            }
        }
    }
}