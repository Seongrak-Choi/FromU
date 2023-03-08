package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.request.PatchFirstMetDayReq
import com.fromu.fromu.data.remote.network.request.PatchMailBoxNameReq
import com.fromu.fromu.data.remote.network.response.PatchFirstMetDayRes
import com.fromu.fromu.data.remote.network.response.PatchMailBoxNameRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH

interface CoupleService {
    @PATCH("couples/mailbox")
    suspend fun getIsMailBoxNameDuplication(@Body patchMailBoxNameResult: PatchMailBoxNameReq): Response<PatchMailBoxNameRes>

    @PATCH("couples/firstMetDay")
    suspend fun patchFirstMetDay(@Body patchFirstMetDayReq: PatchFirstMetDayReq): Response<PatchFirstMetDayRes>
}