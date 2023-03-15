package com.fromu.fromu.data.remote.network.response

import com.fromu.fromu.data.dto.MailListResult

data class MailListRes(
    val result: ArrayList<MailListResult>
) : BaseResponse()
