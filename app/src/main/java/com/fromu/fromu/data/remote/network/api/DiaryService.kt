package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.request.PostDiaryBookReq
import com.fromu.fromu.data.remote.network.response.PatchDiaryBooksPassRes
import com.fromu.fromu.data.remote.network.response.PostDiaryBookRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface DiaryService {

    @POST("diarybooks")
    suspend fun postDiaryBook(@Body postDiaryBookReq: PostDiaryBookReq): Response<PostDiaryBookRes>

    @PATCH("diarybooks/pass")
    suspend fun patchDiaryBooksPss(): Response<PatchDiaryBooksPassRes>
}