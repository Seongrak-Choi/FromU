package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.LoginRes
import com.fromu.fromu.data.repository.LoginRepository
import com.fromu.fromu.ui.base.BaseViewModel
import com.fromu.fromu.utils.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : BaseViewModel() {

    suspend fun login(accessToken: String, loginType: Const.LoginType): LiveData<Resource<LoginRes>> {
        return loginRepository.login(accessToken, loginType).asLiveData()
    }
}