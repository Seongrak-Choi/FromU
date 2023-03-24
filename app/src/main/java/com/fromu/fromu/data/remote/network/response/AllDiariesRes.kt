package com.fromu.fromu.data.remote.network.response

import com.fromu.fromu.data.dto.AllDiariesResult

data class AllDiariesRes(
    val result: ArrayList<AllDiariesResult> //모든 일기를 오름차순으로 저장
) : BaseResponse()
