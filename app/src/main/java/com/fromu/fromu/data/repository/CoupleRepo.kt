package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.CoupleDataSource
import com.fromu.fromu.data.remote.datasource.InvitationDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.PatchMailBoxNameReq
import com.fromu.fromu.data.remote.network.response.CheckMatchingRes
import com.fromu.fromu.data.remote.network.response.PatchMailBoxNameRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class CoupleRepo @Inject constructor(
    private val coupleDataSource: CoupleDataSource,
    private val invitationDataSource: InvitationDataSource
) {

    suspend fun patchMailBoxName(patchMailBoxNameReq: PatchMailBoxNameReq): Flow<Resource<PatchMailBoxNameRes>> {
        return coupleDataSource.patchMailBoxName(patchMailBoxNameReq)
    }


    suspend fun getCoupleInfo(): Flow<Resource<CheckMatchingRes>> {
        return invitationDataSource.getCheckMatching()
    }
}