package com.fromu.fromu.data.remote.network.response

import com.fromu.fromu.data.dto.CreateDiaryResult

data class PostDiaryBookRes(
    val result: CreateDiaryResult
) : BaseResponse()
