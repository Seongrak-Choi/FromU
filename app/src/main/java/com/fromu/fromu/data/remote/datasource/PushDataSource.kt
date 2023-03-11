package com.fromu.fromu.data.remote.datasource

import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.api.PushService
import com.fromu.fromu.data.remote.network.response.PushPartnerRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PushDataSource @Inject constructor(private val pushService: PushService) {

    /**
     * 띵동 벨 울리기
     */
    suspend fun pushPartner(): Flow<Resource<PushPartnerRes>> = flow {
        emit(Resource.Loading)
        val res = pushService.pushPartner()
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("pushPartner: An unkown error occurred"))

    }
}