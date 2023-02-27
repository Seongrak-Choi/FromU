package com.fromu.fromu.viewmodels

import com.fromu.fromu.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class InvitationViewModel : BaseViewModel() {

    //내 코드
    val myCode: MutableStateFlow<String> = MutableStateFlow("")
}