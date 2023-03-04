package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.fromu.fromu.FromUApplication
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.LoginRes
import com.fromu.fromu.data.repository.LoginRepo
import com.fromu.fromu.model.LoginType
import com.fromu.fromu.ui.base.BaseViewModel
import com.fromu.fromu.utils.PrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepo: LoginRepo,
) : BaseViewModel() {

    suspend fun login(accessToken: String, loginType: LoginType): LiveData<Resource<LoginRes>> {
        return loginRepo.login(accessToken, loginType).map { resource ->
            if (resource is Resource.Success) {
                resource.body.result.userInfo?.let { userInfo ->
                    FromUApplication.prefManager.setUserId(userInfo.userId)
                    FromUApplication.prefManager.setLoginToken(userInfo.jwt)
                }
            }
            resource
        }.asLiveData()
    }
}