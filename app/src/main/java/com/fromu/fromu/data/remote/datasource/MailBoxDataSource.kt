package com.fromu.fromu.data.remote.datasource

import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.api.MailBoxService
import com.fromu.fromu.data.remote.network.request.PostLetterReq
import com.fromu.fromu.data.remote.network.request.RateLetterReq
import com.fromu.fromu.data.remote.network.response.MailListRes
import com.fromu.fromu.data.remote.network.response.PostLetterRes
import com.fromu.fromu.data.remote.network.response.RateLetterRes
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

    fun postLetter(postLetterReq: PostLetterReq): Flow<Resource<PostLetterRes>> = flow {
        emit(Resource.Loading)

        val res = mailBoxService.postLetters(postLetterReq)
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("postLetter: An unkown error occurred"))
    }

    fun postReplyLetter(letterId: Int, postLetterReq: PostLetterReq): Flow<Resource<PostLetterRes>> = flow {
        emit(Resource.Loading)

        val res = mailBoxService.postReplyLetters(letterId, postLetterReq)
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("postReplyLetter: An unkown error occurred"))
    }


    /**
     * 별점주기
     */
    fun patchLetters(letterId: Int, rateLetterReq: RateLetterReq): Flow<Resource<RateLetterRes>> = flow {
        emit(Resource.Loading)

        val res = mailBoxService.patchLetter(letterId, rateLetterReq)

        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("patchLetters: An unkown error occurred"))
    }
}