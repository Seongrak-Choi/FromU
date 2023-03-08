package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.CoupleDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.PatchFirstMetDayReq
import com.fromu.fromu.data.remote.network.response.PatchFirstMetDayRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FirstMetDayRepo @Inject constructor(private val coupleDataSource: CoupleDataSource) {
    suspend fun patchFirstMetDay(patchFirstMetDayReq: PatchFirstMetDayReq): Flow<Resource<PatchFirstMetDayRes>> {
        return coupleDataSource.patchFirstMetDay(patchFirstMetDayReq)
    }
}