package com.fromu.fromu.data.remote.datasource

import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.api.InvitationService
import com.fromu.fromu.data.remote.network.request.CouplesPostReq
import com.fromu.fromu.data.remote.network.response.CheckMatchingRes
import com.fromu.fromu.data.remote.network.response.MatchingRes
import com.fromu.fromu.data.remote.network.response.UserInfoRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InvitationDataSource @Inject constructor(private val invitationService: InvitationService) {

    suspend fun getUserInfo(userId: Int): Flow<Resource<UserInfoRes>> = flow {
        emit(Resource.Loading)
        val response = invitationService.getUserInfo(userId)

        if (response.isSuccessful) {
            emit(Resource.Success(response.body()!!))
        } else {
            emit(Resource.Failed(response.message()))
        }

    }.catch {
        emit(Resource.Failed("An unkown error occurred"))
    }


    suspend fun getCheckMatching(): Flow<Resource<CheckMatchingRes>> = flow {
        emit(Resource.Loading)
        val response = invitationService.getCheckingMatch()

        if (response.isSuccessful) {
            emit(Resource.Success(response.body()!!))
        } else {
            emit(Resource.Failed(response.message()))
        }

    }.catch {
        emit(Resource.Failed("An unkown error occurred"))
    }


    suspend fun postMatching(couplesPostReq: CouplesPostReq): Flow<Resource<MatchingRes>> = flow {
        emit(Resource.Loading)

        val response = invitationService.postMatching(couplesPostReq)

        if (response.isSuccessful) {
            emit(Resource.Success(response.body()!!))
        } else {
            emit(Resource.Failed(response.message()))
        }

    }.catch {
        emit(Resource.Failed("An unkown error occurred"))
    }
}