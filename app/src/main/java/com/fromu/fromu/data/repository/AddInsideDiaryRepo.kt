package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.DiaryDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.WriteDiaryRes
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AddInsideDiaryRepo @Inject constructor(private val diaryDataSource: DiaryDataSource) {
    suspend fun writeDiary(imgFile: MultipartBody.Part, data: RequestBody): Flow<Resource<WriteDiaryRes>> {
        return diaryDataSource.writeDiary(imgFile, data)
    }
}