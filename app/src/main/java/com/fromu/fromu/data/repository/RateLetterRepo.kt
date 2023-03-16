package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.MailBoxDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.RateLetterReq
import com.fromu.fromu.data.remote.network.response.RateLetterRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RateLetterRepo @Inject constructor(private val mailBoxDataSource: MailBoxDataSource) {

    fun rateLetter(letterId: Int, rateLetterReq: RateLetterReq): Flow<Resource<RateLetterRes>> {
        return mailBoxDataSource.patchLetters(letterId, rateLetterReq)
    }
}