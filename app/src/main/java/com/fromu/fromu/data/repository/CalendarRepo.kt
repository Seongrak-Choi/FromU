package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.CalendarDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.AddSchedulesReq
import com.fromu.fromu.data.remote.network.request.PatchSchedulesReq
import com.fromu.fromu.data.remote.network.response.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CalendarRepo @Inject constructor(private val calendarDataSource: CalendarDataSource) {

    fun getDetailSchedule(month: String, day: String): Flow<Resource<GetDetailScheduleRes>> {
        return calendarDataSource.getDetailSchedule(month, day)
    }

    fun getSchedulesList(month: String): Flow<Resource<GetSchedulesListRes>> {
        return calendarDataSource.getSchedulesList(month)
    }

    fun addSchedules(addSchedulesReq: AddSchedulesReq): Flow<Resource<AddSchedulesRes>> {
        return calendarDataSource.postSchedules(addSchedulesReq)
    }

    fun deleteSchedules(scheduleId: Int): Flow<Resource<DeleteSchedulesRes>> {
        return calendarDataSource.deleteSchedules(scheduleId)
    }

    fun patchSchedules(scheduleId: Int, patchSchedulesReq: PatchSchedulesReq): Flow<Resource<PatchSchedulesRes>> {
        return calendarDataSource.patchSchedules(scheduleId, patchSchedulesReq)
    }
}