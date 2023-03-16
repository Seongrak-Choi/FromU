package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.RateLetterReq
import com.fromu.fromu.data.remote.network.response.RateLetterRes
import com.fromu.fromu.data.repository.RateLetterRepo
import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RateLetterViewModel @Inject constructor(private val rateLetterRepo: RateLetterRepo) : BaseViewModel() {

    // 선택한 별점
    val startCount: MutableStateFlow<Int> = MutableStateFlow(3)

    // 편지 ID
    val letterId: MutableStateFlow<Int> = MutableStateFlow(0)

    private var _rateLetterResult: MutableLiveData<Resource<RateLetterRes>> = MutableLiveData()
    val rateLetterResult: LiveData<Resource<RateLetterRes>>
        get() = _rateLetterResult

    /**
     * 선택한 별점의 태그를 startCount에 설정
     *
     * @param star
     */
    fun setStartCount(star: Int) {
        startCount.value = star
    }

    /**
     * 별점 주기 api 호출
     */
    fun rateLetter() {
        viewModelScope.launch {
            rateLetterRepo.rateLetter(letterId.value, RateLetterReq(startCount.value)).collect {
                _rateLetterResult.value = it
            }
        }
    }
}