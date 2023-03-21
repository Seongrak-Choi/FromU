package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.remote.network.request.AddSchedulesReq
import com.fromu.fromu.data.remote.network.request.PatchSchedulesReq
import com.fromu.fromu.data.remote.network.response.*
import retrofit2.Response
import retrofit2.http.*

interface CalendarService {

    /**
     *  파라미터에 month(ex-202303)와 date(ex-03)을 넣어 일정을 조회하는 api입니다. month는 필수, date는 필수가 아닙니다.date를 입력하지 않으면 특정 달의 모든 일정을 조회합니다. 기념일이라면 일정의 닉네임은 "우리"입니다.
     *
     * @param month
     * @param date
     */
    @GET("schedules")
    suspend fun getDetailSchedules(@Query("month") month: String, @Query("date") date: String): Response<GetDetailScheduleRes>

    @GET("schedules/list")
    suspend fun getSchedulesList(@Query("month") month: String): Response<GetSchedulesListRes>

    @POST("schedules")
    suspend fun postSchedules(@Body addSchedulesReq: AddSchedulesReq): Response<AddSchedulesRes>

    @DELETE("schedules/{scheduleId}")
    suspend fun deleteSchedule(@Path("scheduleId") scheduleId: Int): Response<DeleteSchedulesRes>

    @PATCH("schedules/{scheduleId}")
    suspend fun patchSchedule(@Path("scheduleId") scheduleId: Int, @Body patchSchedulesReq: PatchSchedulesReq): Response<PatchSchedulesRes>
}