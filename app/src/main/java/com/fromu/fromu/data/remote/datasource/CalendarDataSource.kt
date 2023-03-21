package com.fromu.fromu.data.remote.datasource

import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.api.CalendarService
import com.fromu.fromu.data.remote.network.request.AddSchedulesReq
import com.fromu.fromu.data.remote.network.request.PatchSchedulesReq
import com.fromu.fromu.data.remote.network.response.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CalendarDataSource @Inject constructor(private val calendarService: CalendarService) {

    fun getDetailSchedule(month: String, day: String): Flow<Resource<GetDetailScheduleRes>> = flow {
        emit(Resource.Loading)

        val res = calendarService.getDetailSchedules(month, day)

        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("getSchedule : An unkown error occurred"))
    }

    fun getSchedulesList(month: String): Flow<Resource<GetSchedulesListRes>> = flow {
        emit(Resource.Loading)

        val res = calendarService.getSchedulesList(month)

        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("getSchedulesList : An unkown error occurred"))
    }


    fun postSchedules(addSchedulesReq: AddSchedulesReq): Flow<Resource<AddSchedulesRes>> = flow {
        emit(Resource.Loading)

        val res = calendarService.postSchedules(addSchedulesReq)

        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("postSchedules : An unkown error occurred"))
    }


    fun deleteSchedules(scheduleId: Int): Flow<Resource<DeleteSchedulesRes>> = flow {
        emit(Resource.Loading)

        val res = calendarService.deleteSchedule(scheduleId)

        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("deleteSchedules : An unkown error occurred"))
    }

    fun patchSchedules(scheduleId: Int, patchSchedulesReq: PatchSchedulesReq): Flow<Resource<PatchSchedulesRes>> = flow {
        emit(Resource.Loading)

        val res = calendarService.patchSchedule(scheduleId, patchSchedulesReq)

        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("patchSchedules : An unkown error occurred"))
    }
}