package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.response.DiaryViewRes
import retrofit2.Response
import retrofit2.http.GET

interface ViewService {

    @GET("view/main")
    suspend fun getDiaryView(): Response<DiaryViewRes>
}