package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.response.CheckMatchingRes
import com.fromu.fromu.data.remote.network.response.MatchingRes
import com.fromu.fromu.data.remote.network.response.UserInfoRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface InvitationService {
    @GET("users/{userId}")
    suspend fun getUserInfo(@Path("userId") userId: Int): Response<UserInfoRes>

    @GET("couples/isMatch")
    suspend fun getCheckingMatch(): Response<CheckMatchingRes>

    @POST("couples")
    suspend fun postMatching(@Body opponentCode: String): Response<MatchingRes>
}