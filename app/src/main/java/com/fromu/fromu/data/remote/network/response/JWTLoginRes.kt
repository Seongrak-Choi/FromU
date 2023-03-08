package com.fromu.fromu.data.remote.network.response

import com.fromu.fromu.data.dto.UserInfo

data class JWTLoginRes(
    val result: UserInfo?
) : BaseResponse()
