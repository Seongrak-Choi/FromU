package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.response.DiaryViewRes
import com.fromu.fromu.data.remote.network.response.MailBoxViewRes
import retrofit2.Response
import retrofit2.http.GET

interface ViewService {

    @GET("view/main")
    suspend fun getDiaryView(): Response<DiaryViewRes>

    @GET("view/mailbox")
    suspend fun getMailBoxView(): Response<MailBoxViewRes>
}