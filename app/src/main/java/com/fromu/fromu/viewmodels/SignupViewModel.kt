package com.fromu.fromu.viewmodels

import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor() : BaseViewModel() {
    // 입력한 nickName 값
    val nickname: MutableStateFlow<String> = MutableStateFlow("")

    // nickName의 유효성 타당 결과
    val isNicknameMatch: MutableStateFlow<Boolean> = MutableStateFlow(false)
}

