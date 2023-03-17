package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.MailBoxDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.ReportLetterReq
import com.fromu.fromu.data.remote.network.response.ReportLetterRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReportLetterRepo @Inject constructor(private val mailBoxDataSource: MailBoxDataSource) {

    fun reportLetter(letterId: Int, reportLetterReq: ReportLetterReq): Flow<Resource<ReportLetterRes>> {
        return mailBoxDataSource.postReportLetter(letterId, reportLetterReq)
    }
}