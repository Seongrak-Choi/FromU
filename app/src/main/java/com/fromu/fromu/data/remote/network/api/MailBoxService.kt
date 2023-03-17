package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.request.PostLetterReq
import com.fromu.fromu.data.remote.network.request.RateLetterReq
import com.fromu.fromu.data.remote.network.request.ReportLetterReq
import com.fromu.fromu.data.remote.network.response.*
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

    @PATCH("letters/{letterId}/score")
    suspend fun patchLetter(@Path("letterId") letterId: Int, @Body rateLetterReq: RateLetterReq): Response<RateLetterRes>

    @POST("letters/{letterId}/report")
    suspend fun postReportLetter(@Path("letterId") letterId: Int, @Body reportLetterReq: ReportLetterReq): Response<ReportLetterRes>
}