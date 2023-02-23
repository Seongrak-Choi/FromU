package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.LoginDataSource
import com.fromu.fromu.utils.Const
import javax.inject.Inject


class LoginRepository @Inject constructor(private val loginDataSource: LoginDataSource) {
    fun login(accessToken: String, loginType: Const.LoginType) {

        when (loginType) {
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