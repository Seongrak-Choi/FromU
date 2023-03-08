package com.fromu.fromu.data.remote.datasource

import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.api.CoupleService
import com.fromu.fromu.data.remote.network.request.PatchFirstMetDayReq
import com.fromu.fromu.data.remote.network.request.PatchMailBoxNameReq
import com.fromu.fromu.data.remote.network.response.PatchFirstMetDayRes
import com.fromu.fromu.data.remote.network.response.PatchMailBoxNameRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CoupleDataSource @Inject constructor(private val coupleService: CoupleService) {

    /**
     * 우편함 이름 설정
     *
     * @param patchMailBoxNameReq
     * @return
     */
    suspend fun patchMailBoxName(patchMailBoxNameReq: PatchMailBoxNameReq): Flow<Resource<PatchMailBoxNameRes>> = flow {
        emit(Resource.Loading)

        val res = coupleService.getIsMailBoxNameDuplication(patchMailBoxNameReq)
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("patchMailBoxName : An unkown error occurred"))
    }


    suspend fun patchFirstMetDay(patchFirstMetDayReq: PatchFirstMetDayReq): Flow<Resource<PatchFirstMetDayRes>> = flow<Resource<PatchFirstMetDayRes>> {
        emit(Resource.Loading)

        val res = coupleService.patchFirstMetDay(patchFirstMetDayReq)
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("patchFirstMetDay : An unkown error occurred"))
    }
}