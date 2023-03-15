package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.response.MailListRes
import com.fromu.fromu.data.remote.network.response.ReadLetterRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface MailBoxService {

    @GET("letters/mailbox")
    suspend fun getMailList(@Query("type") type: String): Response<MailListRes>

    @PATCH("letters/{letterId}/read")
    suspend fun patchLetterRead(@Path("letterId") letterId: Int): Response<ReadLetterRes>
}