package com.fromu.fromu.data.remote.network.response

import com.fromu.fromu.data.dto.GetNoticeResult

data class GetNoticeRes(
    val result: ArrayList<GetNoticeResult>
): BaseResponse()
