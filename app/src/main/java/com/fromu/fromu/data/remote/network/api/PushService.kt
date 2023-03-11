package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.response.PushPartnerRes
import retrofit2.Response
import retrofit2.http.POST

interface PushService {
    @POST("push/partner")
    suspend fun pushPartner(): Response<PushPartnerRes>

}