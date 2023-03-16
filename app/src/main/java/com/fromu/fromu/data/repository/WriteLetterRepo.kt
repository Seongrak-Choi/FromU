package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.MailBoxDataSource
import com.fromu.fromu.data.remote.datasource.ViewDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.PostLetterReq
import com.fromu.fromu.data.remote.network.response.PostLetterRes
import com.fromu.fromu.data.remote.network.response.StampCountRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WriteLetterRepo @Inject constructor(
    private val viewDataSource: ViewDataSource,
    private val mailBoxDataSource: MailBoxDataSource
) {

    fun getStampList(): Flow<Resource<StampCountRes>> {
        return viewDataSource.getStampList()
    }

    fun postLetter(postLetterReq: PostLetterReq): Flow<Resource<PostLetterRes>> {
        return mailBoxDataSource.postLetter(postLetterReq)
    }

    fun postReplyLetter(letterId: Int, postLetterReq: PostLetterReq): Flow<Resource<PostLetterRes>> {
        return mailBoxDataSource.postReplyLetter(letterId, postLetterReq)
    }
}