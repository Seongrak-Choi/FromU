package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.LoginDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.LoginRes
import com.fromu.fromu.model.LoginType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class LoginRepo @Inject constructor(private val loginDataSource: LoginDataSource) {
    suspend fun login(accessToken: String, loginType: LoginType): Flow<Resource<LoginRes>> {
        return when (loginType) {
            LoginType.JWT -> {
                loginDataSource.loginWithJwt()
            }
            LoginType.GOOGLE -> {
                loginDataSource.loginWithGoogle(accessToken)
            }
            LoginType.KAKAO -> {
                loginDataSource.loginWithKakao(accessToken)
            }
        }
    }
}