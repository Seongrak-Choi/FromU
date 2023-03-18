package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.ViewDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.FromCountRes
import com.fromu.fromu.data.remote.network.response.GetNoticeRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepo @Inject constructor(private val viewDataSource: ViewDataSource) {
    fun getFromCount(): Flow<Resource<FromCountRes>> {
        return viewDataSource.getFromCount()
    }

    fun getNotice(): Flow<Resource<GetNoticeRes>> {
        return viewDataSource.getNotice()
    }
}