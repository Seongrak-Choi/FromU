package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.LoginDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.JWTLoginRes
import com.fromu.fromu.data.remote.network.response.SNSLoginRes
import com.fromu.fromu.model.LoginType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class LoginRepo @Inject constructor(private val loginDataSource: LoginDataSource) {
    suspend fun loginWithSns(accessToken: String, loginType: LoginType): Flow<Resource<SNSLoginRes>> {
        return when (loginType) {
            LoginType.GOOGLE -> {
                loginDataSource.loginWithGoogle(accessToken)
            }
            LoginType.KAKAO -> {
                loginDataSource.loginWithKakao(accessToken)
            }
        }
    }

    suspend fun loginWithJwt(jwt: String): Flow<Resource<JWTLoginRes>> {
        return loginDataSource.loginWithJwt(jwt)
    }
}