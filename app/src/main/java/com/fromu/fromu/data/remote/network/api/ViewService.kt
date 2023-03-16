package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.response.DiaryViewRes
import com.fromu.fromu.data.remote.network.response.FromCountRes
import com.fromu.fromu.data.remote.network.response.MailBoxViewRes
import com.fromu.fromu.data.remote.network.response.StampCountRes
import retrofit2.Response
import retrofit2.http.GET

interface ViewService {

    @GET("view/fromCount")
    suspend fun getFromCount(): Response<FromCountRes>

    @GET("view/main")
    suspend fun getDiaryView(): Response<DiaryViewRes>

    @GET("view/mailbox")
    suspend fun getMailBoxView(): Response<MailBoxViewRes>

    @GET("view/stampCount")
    suspend fun getStampCount(): Response<StampCountRes>

    @GET("view/stampList")
    suspend fun getStampList(): Response<StampCountRes>
}