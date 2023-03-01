package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.response.LoginResponse
import com.fromu.fromu.utils.Const
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {
    @POST("users/kakao")
    suspend fun kakaoLogin(@Header(Const.LOGIN_ACCESS_TOKEN) header: String): Response<LoginResponse>
}