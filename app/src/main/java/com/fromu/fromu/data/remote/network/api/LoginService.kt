package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.response.JWTLoginRes
import com.fromu.fromu.data.remote.network.response.SNSLoginRes
import com.fromu.fromu.utils.PrefManager
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {
    companion object {
        /* 소셜 로그인 시 header로 전달할 accessToken Name */
        const val LOGIN_ACCESS_TOKEN = "LOGIN-ACCESS-TOKEN"
    }

    @POST("users/kakao")
    suspend fun kakaoLogin(@Header(PrefManager.X_ACCESS_TOKEN) header: String): Response<SNSLoginRes>

    /**
     * jwt로 로그인하고 실패 시 refreshToken으로 로그인
     *
     * @param header
     * @return
     */
    @POST("users/refreshToken")
    suspend fun jwtLogin(@Header(PrefManager.X_ACCESS_TOKEN) header: String): Response<JWTLoginRes>
}