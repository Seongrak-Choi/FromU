package com.fromu.fromu.viewmodels

import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class FirstMetDayViewModel @Inject constructor() :BaseViewModel() {

    // 처음 만난 날 값 ex) 19960210
    val firstMetDay: MutableStateFlow<String> = MutableStateFlow("")

    // 처음 만난 날 유효성 확인 결과
    val isValidFirstMetDay: MutableStateFlow<Boolean> = MutableStateFlow(false)

}