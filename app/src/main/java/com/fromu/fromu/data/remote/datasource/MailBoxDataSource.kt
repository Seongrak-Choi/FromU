package com.fromu.fromu.data.remote.datasource

import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.api.MailBoxService
import com.fromu.fromu.data.remote.network.response.MailListRes
import com.fromu.fromu.data.remote.network.response.ReadLetterRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MailBoxDataSource @Inject constructor(private val mailBoxService: MailBoxService) {
    fun getMailList(type: String): Flow<Resource<MailListRes>> = flow {
        emit(Resource.Loading)

        val res = mailBoxService.getMailList(type)

        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("getMailList: An unkown error occurred"))
    }


    fun patchLetterRead(letterId: Int): Flow<Resource<ReadLetterRes>> = flow {
        emit(Resource.Loading)

        val res = mailBoxService.patchLetterRead(letterId)
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("patchLetterRead: An unkown error occurred"))
    }

}