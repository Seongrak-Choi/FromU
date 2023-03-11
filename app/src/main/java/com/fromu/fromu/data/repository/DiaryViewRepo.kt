package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.DiaryDataSource
import com.fromu.fromu.data.remote.datasource.PushDataSource
import com.fromu.fromu.data.remote.datasource.ViewDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.DiaryViewRes
import com.fromu.fromu.data.remote.network.response.PushPartnerRes
import com.fromu.fromu.data.remote.network.response.SendDiaryBooksRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DiaryViewRepo @Inject constructor(
    private val viewDataSource: ViewDataSource,
    private val diaryDataSource: DiaryDataSource,
    private val pushDataSource: PushDataSource
) {
    suspend fun getDiaryView(): Flow<Resource<DiaryViewRes>> {
        return viewDataSource.getDiaryView()
    }

    suspend fun sendDiaryBooks(): Flow<Resource<SendDiaryBooksRes>> {
        return diaryDataSource.sendDiaryBooks()
    }

    suspend fun pushPartner(): Flow<Resource<PushPartnerRes>> {
        return pushDataSource.pushPartner()
    }
}