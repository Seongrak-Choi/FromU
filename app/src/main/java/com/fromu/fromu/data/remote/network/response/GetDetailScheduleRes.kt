package com.fromu.fromu.data.remote.network.response

import com.fromu.fromu.data.dto.DetailScheduleResult

data class GetDetailScheduleRes(
    val result: ArrayList<DetailScheduleResult>
) : BaseResponse()