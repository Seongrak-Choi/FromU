package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.LoginResponse
import com.fromu.fromu.data.repository.LoginRepository
import com.fromu.fromu.ui.base.BaseViewModel
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : BaseViewModel() {

    suspend fun login(accessToken: String, loginType: Const.LoginType): LiveData<Resource<LoginResponse>> {
        Logger.e("rak", "viewmodel - login")
        return loginRepository.login(accessToken, loginType).asLiveData()
    }
}