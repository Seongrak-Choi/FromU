package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fromu.fromu.FromUApplication
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.JWTLoginRes
import com.fromu.fromu.data.remote.network.response.SNSLoginRes
import com.fromu.fromu.data.repository.LoginRepo
import com.fromu.fromu.model.LoginType
import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepo: LoginRepo,
) : BaseViewModel() {

    // refresh 로그인 결과과
    private var _loginWithRefreshTokenResult: MutableLiveData<Resource<JWTLoginRes>> = MutableLiveData()
    val loginWithRefreshTokenResult: LiveData<Resource<JWTLoginRes>>
        get() = _loginWithRefreshTokenResult

    suspend fun loginWithSns(accessToken: String, loginType: LoginType): LiveData<Resource<SNSLoginRes>> {
        return loginRepo.loginWithSns(accessToken, loginType).map { resource ->
            if (resource is Resource.Success) {
                resource.body.result.userInfo?.let { userInfo ->
                    FromUApplication.prefManager.setUserId(userInfo.userId)
                    FromUApplication.prefManager.setLoginToken(userInfo.jwt)
                    FromUApplication.prefManager.setRefreshToken(userInfo.refreshToken)
                }
            }
            resource
        }.asLiveData()
    }

    fun loginWithRefreshToken() {
        viewModelScope.launch {
            FromUApplication.prefManager.getRefreshToken()?.let { it ->
                loginRepo.loginWithJwt(it).map { resource ->
                    if (resource is Resource.Success) {
                        resource.body.result?.let {
                            FromUApplication.prefManager.setUserId(it.userId)
                            FromUApplication.prefManager.setLoginToken(it.jwt)
                            FromUApplication.prefManager.setRefreshToken(it.refreshToken)
                        }
                    }
                    resource
                }.collect {
                    _loginWithRefreshTokenResult.value = it
                }
            }
        }
    }

}