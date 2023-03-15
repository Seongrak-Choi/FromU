package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.MailBoxDataSource
import com.fromu.fromu.data.remote.datasource.ViewDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.MailBoxViewRes
import com.fromu.fromu.data.remote.network.response.ReadLetterRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MailBoxRepo @Inject constructor(
    private val viewDataSource: ViewDataSource,
    private val mailBoxDataSource: MailBoxDataSource
) {
    fun getMailBoxView(): Flow<Resource<MailBoxViewRes>> {
        return viewDataSource.getMailBoxView()
    }

    /**
     * 편지 데이터 조회 및 읽음 처리
     *
     * @param letterId
     * @return
     */
    fun readLetter(letterId: Int): Flow<Resource<ReadLetterRes>> {
        return mailBoxDataSource.patchLetterRead(letterId)
    }

}