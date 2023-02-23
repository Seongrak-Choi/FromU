package com.fromu.fromu.viewmodel

import com.fromu.fromu.data.repository.LoginRepository
import com.fromu.fromu.ui.base.BaseViewModel
import com.fromu.fromu.utils.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : BaseViewModel() {

    fun login(accessToken: String, loginType: Const.LoginType) {
        loginRepository.login(accessToken, loginType)
    }
}