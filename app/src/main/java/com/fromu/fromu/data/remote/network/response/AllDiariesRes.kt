package com.fromu.fromu.data.remote.network.response

data class AllDiariesRes(
    val result: ArrayList<Int> //모든 일기를 오름차순으로 저장
) : BaseResponse()
