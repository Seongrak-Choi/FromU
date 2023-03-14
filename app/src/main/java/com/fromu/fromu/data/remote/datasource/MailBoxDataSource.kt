package com.fromu.fromu.data.remote.datasource

import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.api.ViewService
import com.fromu.fromu.data.remote.network.response.MailBoxViewRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MailBoxDataSource @Inject constructor(private val viewService: ViewService) {
    fun getMailBoxView(): Flow<Resource<MailBoxViewRes>> = flow {
        emit(Resource.Loading)

        val res = viewService.getMailBoxView()
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("getMailBoxView: An unkown error occurred"))
    }
}