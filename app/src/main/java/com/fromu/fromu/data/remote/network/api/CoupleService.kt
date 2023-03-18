package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.request.PatchFirstMetDayReq
import com.fromu.fromu.data.remote.network.request.PatchMailBoxNameReq
import com.fromu.fromu.data.remote.network.request.SetBellMsgReq
import com.fromu.fromu.data.remote.network.response.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface CoupleService {
    @PATCH("couples/mailbox")
    suspend fun getIsMailBoxNameDuplication(@Body patchMailBoxNameResult: PatchMailBoxNameReq): Response<PatchMailBoxNameRes>

    @GET("couples/isMatch")
    suspend fun getCheckingMatch(): Response<CheckMatchingRes>

    @PATCH("couples/firstMetDay")
    suspend fun patchFirstMetDay(@Body patchFirstMetDayReq: PatchFirstMetDayReq): Response<PatchFirstMetDayRes>

    @PATCH("couples/d")
    suspend fun breakMatching(): Response<BreakMatchingRes>

    @GET("couples/stamp/{stampNum}")
    suspend fun getCouplesStamp(@Path("stampNum") stampNum: Int): Response<PurchaseStampRes>

    @PATCH("couples/pushMessage")
    suspend fun patchPushMessage(@Body setBellMsgReq: SetBellMsgReq): Response<SetBellMsgRes>

    @GET("couples/pushMessage")
    suspend fun getPushMessage(): Response<GetBellMsgRes>
}