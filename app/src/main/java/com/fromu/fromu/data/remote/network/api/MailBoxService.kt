package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.request.PostLetterReq
import com.fromu.fromu.data.remote.network.response.MailListRes
import com.fromu.fromu.data.remote.network.response.PostLetterRes
import com.fromu.fromu.data.remote.network.response.ReadLetterRes
import retrofit2.Response
import retrofit2.http.*

interface MailBoxService {

    @GET("letters/mailbox")
    suspend fun getMailList(@Query("type") type: String): Response<MailListRes>

    @PATCH("letters/{letterId}/read")
    suspend fun patchLetterRead(@Path("letterId") letterId: Int): Response<ReadLetterRes>

    @POST("letters")
    suspend fun postLetters(@Body postLetterReq: PostLetterReq): Response<PostLetterRes>

    @POST("letters/{letterId}/reply")
    suspend fun postReplyLetters(@Path("letterId") letterId: Int, @Body postLetterReq: PostLetterReq): Response<PostLetterRes>
}