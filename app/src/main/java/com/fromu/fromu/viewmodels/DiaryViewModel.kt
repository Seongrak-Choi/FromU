package com.fromu.fromu.viewmodels

import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor() : BaseViewModel() {
    val myNickname: MutableStateFlow<String> = MutableStateFlow("아라")
    val partnerNickname: MutableStateFlow<String> = MutableStateFlow("은다")
    val dday: MutableStateFlow<String> = MutableStateFlow("00")
    // 디데이 설정 유무
    val isSetFirstMetDay: MutableStateFlow<Boolean> = MutableStateFlow(false)
    // 일기장 생성 유무
    val isHaveDiary: MutableStateFlow<Boolean> = MutableStateFlow(false)
}