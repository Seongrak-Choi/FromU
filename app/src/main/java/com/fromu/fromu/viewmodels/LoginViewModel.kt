package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fromu.fromu.FromUApplication
import com.fromu.fromu.data.dto.GoogleSignInAccessTokenDataClass
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.PatchFcmTokenReq
import com.fromu.fromu.data.remote.network.response.JWTLoginRes
import com.fromu.fromu.data.remote.network.response.SNSLoginRes
import com.fromu.fromu.data.repository.LoginRepo
import com.fromu.fromu.model.LoginType
import com.fromu.fromu.ui.base.BaseViewModel
import com.fromu.fromu.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepo: LoginRepo,
) : BaseViewModel() {

    // refresh 로그인 결과
    private var _loginWithRefreshTokenResult: MutableLiveData<Resource<JWTLoginRes>> = MutableLiveData()
    val loginWithRefreshTokenResult: LiveData<Resource<JWTLoginRes>>
        get() = _loginWithRefreshTokenResult

    // 구글 accessToken 반환 결과
    private var _getGoogleAccessTokenResult: MutableLiveData<Resource<GoogleSignInAccessTokenDataClass>> = MutableLiveData()
    val getGoogleAccessTokenResult: LiveData<Resource<GoogleSignInAccessTokenDataClass>>
        get() = _getGoogleAccessTokenResult

    suspend fun loginWithSns(accessToken: String, loginType: LoginType): LiveData<Resource<SNSLoginRes>> {
        return loginRepo.loginWithSns(accessToken, loginType).map { resource ->
            if (resource is Resource.Success) {
                resource.body.result.userInfo?.let { userInfo ->
                    FromUApplication.prefManager.setUserId(userInfo.userId)
                    FromUApplication.prefManager.setLoginToken(userInfo.jwt)
                    FromUApplication.prefManager.setRefreshToken(userInfo.refreshToken)

                    patchFcmToken(userInfo.jwt)
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
                        resource.body.result?.let { userInfo ->
                            FromUApplication.prefManager.setUserId(userInfo.userId)
                            FromUApplication.prefManager.setLoginToken(userInfo.jwt)
                            FromUApplication.prefManager.setRefreshToken(userInfo.refreshToken)

                            patchFcmToken(userInfo.jwt)
                        }
                    }
                    resource
                }.collect {
                    _loginWithRefreshTokenResult.value = it
                }
            } ?: let {
                Logger.e("tokenLogin", "토큰이 없기 때문에 토큰 로그인 불가")
            }
        }
    }


    /**
     * 구글 로그인 성공 시 받는 autoCode와 idToken으로 accessToken 가져오기
     *
     * @param authCode
     * @param idToken
     */
    fun getGoogleAccessToken(authCode: String, idToken: String) {
        viewModelScope.launch {
            loginRepo.getGoogleAccessToken(authCode, idToken).collect {
                _getGoogleAccessTokenResult.value = it
            }
        }
    }

    /**
     * fcm 토큰 서버에 전송 api
     *
     */
    private fun patchFcmToken(jwt: String) {
        loginRepo.patchFcmToken(jwt, PatchFcmTokenReq(FromUApplication.prefManager.getFcmId()))
    }
}