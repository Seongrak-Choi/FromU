package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.LoginDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.LoginRes
import com.fromu.fromu.utils.Const
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class LoginRepository @Inject constructor(private val loginDataSource: LoginDataSource) {
    suspend fun login(accessToken: String, loginType: Const.LoginType): Flow<Resource<LoginRes>> {
        return when (loginType) {
            Const.LoginType.JWT -> {
                loginDataSource.loginWithJwt()
            }
            Const.LoginType.GOOGLE -> {
                loginDataSource.loginWithGoogle(accessToken)
            }
            Const.LoginType.KAKAO -> {
                loginDataSource.loginWithKakao(accessToken)
            }
        }
    }
}