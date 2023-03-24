package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.request.CreateDiaryBookReq
import com.fromu.fromu.data.remote.network.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface DiaryService {

    @GET("diarybooks/firstPage")
    suspend fun getFirstPage(): Response<FirstPageRes>

    //일기장 등록
    @POST("diarybooks")
    suspend fun postDiaryBook(@Body createDiaryBookReq: CreateDiaryBookReq): Response<CreateDiaryBookRes>

    //일기장 보내기
    @PATCH("diarybooks/pass")
    suspend fun patchDiaryBooksPss(): Response<SendDiaryBooksRes>

    // 일기 등록
    @Multipart
    @POST("diaries")
    suspend fun postDiaries(@Part imageFile: MultipartBody.Part, @Part("postDiaryReq") data: RequestBody): Response<WriteDiaryRes>

    @Multipart
    @POST("diaries/{diaryId}")
    suspend fun patchDiaries(@Path("diaryId") diaryId: Int, @Part imageFile: MultipartBody.Part?, @Part("patchDiaryReq") patchDiaryReq: RequestBody): Response<EditDiaryRes>


    // 일기 전체 조회
    @GET("diaries/all/{diarybookId}/withUserId")
    suspend fun getAllDiary(@Path("diarybookId") diaryBookId: Int): Response<AllDiariesRes>

    // 일기 디테일 조회 (리스너 용)
    @GET("diaries/{diaryId}")
    fun getDetailDiaryForListener(@Path("diaryId") diaryId: Int): Call<DetailDiaryRes>

    // 일기 디테일 조회
    @GET("diaries/{diaryId}")
    suspend fun getDetailDiary(@Path("diaryId") diaryId: Int): Response<DetailDiaryRes>

    //일기장 내지 첫장 대표 이미지 변경
    @Multipart
    @PATCH("diarybooks/image")
    suspend fun patchDiaryBooksImage(@Part imageFile: MultipartBody.Part): Response<ChangeFirstPageImgRes>

    // 목차 월 챕터 조회 api
    @GET("diaries/monthList/{diarybookId}")
    suspend fun getMonthList(@Path("diarybookId") diaryBookId: Int): Response<IndexMonthListRes>

    // 목차 특정 월에 있는 데이터 조회 api
    @GET("diaries/byMonth/{diarybookId}")
    suspend fun getDiaryByMonth(@Path("diarybookId") diaryBookId: Int, @Query("month") month: String): Response<IndexByMonthRes>

    @PATCH("diaries/{diaryId}/d")
    suspend fun deleteDiary(@Path("diaryId") diaryId: Int): Response<DeleteDiaryRes>
}