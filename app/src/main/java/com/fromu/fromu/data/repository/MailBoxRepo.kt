package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.MailBoxDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.MailBoxViewRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MailBoxRepo @Inject constructor(private val mailBoxDataSource: MailBoxDataSource) {
    fun getMailBoxView(): Flow<Resource<MailBoxViewRes>> {
        return mailBoxDataSource.getMailBoxView()
    }
}