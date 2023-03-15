package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.MailBoxDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.MailListRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MailListRepo @Inject constructor(private val mailBoxDataSource: MailBoxDataSource) {
    enum class GetMailListType(val type: String) {
        RECEIVE("0"), SEND("1")
    }

    fun getSendMailList(): Flow<Resource<MailListRes>> {
        return mailBoxDataSource.getMailList(GetMailListType.SEND.type)
    }

    fun getReceiveMailList(): Flow<Resource<MailListRes>> {
        return mailBoxDataSource.getMailList(GetMailListType.RECEIVE.type)
    }
}