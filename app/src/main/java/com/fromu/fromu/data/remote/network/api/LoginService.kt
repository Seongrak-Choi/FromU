package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.response.LoginRes
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {
    companion object {
        /* 소셜 로그인 시 header로 전달할 accessToken Name */
        const val LOGIN_ACCESS_TOKEN = "LOGIN-ACCESS-TOKEN"
    }

    @POST("users/kakao")
    suspend fun kakaoLogin(@Header(LOGIN_ACCESS_TOKEN) header: String): Response<LoginRes>
}