package com.fromu.fromu.data.remote.datasource

import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.api.ViewService
import com.fromu.fromu.data.remote.network.response.DiaryViewRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ViewDataSource @Inject constructor(private val viewService: ViewService) {

    suspend fun getDiaryView(): Flow<Resource<DiaryViewRes>> = flow {
        emit(Resource.Loading)

        val res = viewService.getDiaryView()

        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("getDiaryView : An unkown error occurred"))
    }
}