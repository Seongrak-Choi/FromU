package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.CoupleDataSource
import com.fromu.fromu.data.remote.datasource.UserDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.BreakMatchingRes
import com.fromu.fromu.data.remote.network.response.CheckMatchingRes
import com.fromu.fromu.data.remote.network.response.LogoutRes
import com.fromu.fromu.data.remote.network.response.WithdrawalRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MyHomeRepo @Inject constructor(
    private val coupleDataSource: CoupleDataSource,
    private val userDataSource: UserDataSource,
) {

    suspend fun getCoupleInfo(): Flow<Resource<CheckMatchingRes>> {
        return coupleDataSource.getCheckMatching()
    }

    suspend fun logout(): Flow<Resource<LogoutRes>> {
        return userDataSource.logout()
    }

    suspend fun breakMatching(): Flow<Resource<BreakMatchingRes>> {
        return coupleDataSource.breakMatching()
    }

    suspend fun withdrawal(): Flow<Resource<WithdrawalRes>> {
        return userDataSource.withdrawal()
    }

}