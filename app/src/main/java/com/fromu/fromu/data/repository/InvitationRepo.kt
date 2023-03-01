package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.InvitationDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.CheckMatchingRes
import com.fromu.fromu.data.remote.network.response.MatchingRes
import com.fromu.fromu.data.remote.network.response.UserInfoRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InvitationRepo @Inject constructor(private val invitationDataSource: InvitationDataSource) {

    suspend fun getUserInfo(userId: Int): Flow<Resource<UserInfoRes>> {
        return invitationDataSource.getUserInfo(userId)
    }

    suspend fun getCheckMatching(): Flow<Resource<CheckMatchingRes>> {
        return invitationDataSource.getCheckMatching()
    }

    suspend fun postMatching(opponentCode: String): Flow<Resource<MatchingRes>> {
        return invitationDataSource.postMatching(opponentCode)
    }
}