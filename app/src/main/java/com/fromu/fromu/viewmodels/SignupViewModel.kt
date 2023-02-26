package com.fromu.fromu.viewmodels

import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor() : BaseViewModel() {
    // 입력한 nickName 값
    val nickname: MutableStateFlow<String> = MutableStateFlow("")

    // nickName의 유효성 확인결과
    val isValidNickname: MutableStateFlow<Boolean> = MutableStateFlow(false)


    // 입력한 생년월일 값 ex) 19960210
    val birthday: MutableStateFlow<String> = MutableStateFlow("")

    // 생년월일 유효성 확인 결과
    val isValidBirthday: MutableStateFlow<Boolean> = MutableStateFlow(false)

    // 선택한 Gender 값
    val gender: MutableStateFlow<String> = MutableStateFlow("")


    fun setGender(value: String) {
        gender.value = value
    }
}

