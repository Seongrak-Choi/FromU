package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.CoupleDataSource
import com.fromu.fromu.data.remote.datasource.UserDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.SetBellMsgReq
import com.fromu.fromu.data.remote.network.response.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MyHomeRepo @Inject constructor(
    private val coupleDataSource: CoupleDataSource,
    private val userDataSource: UserDataSource,
) {

    suspend fun getCoupleInfo(): Flow<Resource<CheckMatchingRes>> {
        return coupleDataSource.getCheckMatching()
    }

    fun logout(): Flow<Resource<LogoutRes>> {
        return userDataSource.logout()
    }

    fun breakMatching(): Flow<Resource<BreakMatchingRes>> {
        return coupleDataSource.breakMatching()
    }

    fun withdrawal(): Flow<Resource<WithdrawalRes>> {
        return userDataSource.withdrawal()
    }

    fun setBellMsg(setBellMsgReq: SetBellMsgReq): Flow<Resource<SetBellMsgRes>> {
        return coupleDataSource.patchPushMessage(setBellMsgReq)
    }

    fun getBellMsg(): Flow<Resource<GetBellMsgRes>> {
        return coupleDataSource.getPushMessage()
    }


}