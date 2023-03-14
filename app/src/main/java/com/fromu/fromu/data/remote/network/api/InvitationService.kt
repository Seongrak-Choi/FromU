package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.request.CouplesPostReq
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

    @POST("couples")
    suspend fun postMatching(@Body couplesPostReq: CouplesPostReq): Response<MatchingRes>
}