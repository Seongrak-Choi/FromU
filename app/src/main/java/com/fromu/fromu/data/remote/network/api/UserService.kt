package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.response.LogoutRes
import com.fromu.fromu.data.remote.network.response.WithdrawalRes
import retrofit2.Response
import retrofit2.http.PATCH

interface UserService {
    @PATCH("users/logout")
    suspend fun logout(): Response<LogoutRes>

    @PATCH("users/d")
    suspend fun withdrawal(): Response<WithdrawalRes>
}