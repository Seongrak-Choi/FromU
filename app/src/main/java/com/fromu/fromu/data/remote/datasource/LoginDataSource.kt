package com.fromu.fromu.data.remote.datasource

import javax.inject.Inject


class LoginDataSource @Inject constructor() {
    fun loginWithKakao(accessToken: String) {
        //TODO accessToken 서버와 연결
    }

    fun loginWithGoogle(accessToken: String) {
        //TODO accessToken 서버와 연결
    }

    fun loginWithJwt() {
        //TODO 자동 로그인 api 연동
    }
}