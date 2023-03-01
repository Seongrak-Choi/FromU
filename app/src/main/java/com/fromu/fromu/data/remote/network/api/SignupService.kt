package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.request.SignupReq
import com.fromu.fromu.data.remote.network.response.SignupRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignupService {
    @POST("users")
    suspend fun postSignup(@Body signupReq: SignupReq): Response<SignupRes>
}