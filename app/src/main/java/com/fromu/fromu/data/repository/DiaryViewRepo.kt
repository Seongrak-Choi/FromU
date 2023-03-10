package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.DiaryDataSource
import com.fromu.fromu.data.remote.datasource.ViewDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.DiaryViewRes
import com.fromu.fromu.data.remote.network.response.SendDiaryBooksRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DiaryViewRepo @Inject constructor(
    private val viewDataSource: ViewDataSource,
    private val diaryDataSource: DiaryDataSource
) {
    suspend fun getDiaryView(): Flow<Resource<DiaryViewRes>> {
        return viewDataSource.getDiaryView()
    }

    suspend fun sendDiaryBooks(): Flow<Resource<SendDiaryBooksRes>> {
        return diaryDataSource.sendDiaryBooks()
    }
}