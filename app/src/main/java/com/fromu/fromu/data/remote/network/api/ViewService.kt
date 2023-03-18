package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.response.*
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

    @GET("view/notice")
    suspend fun getNotice(): Response<GetNoticeRes>
}