package com.fromu.fromu.model.listener

import com.fromu.fromu.data.remote.network.response.DetailDiaryRes

interface DetailDiaryListener {
    fun onSuccess(detailDiaryRes: DetailDiaryRes)
    fun onFailure(error: Throwable)
}