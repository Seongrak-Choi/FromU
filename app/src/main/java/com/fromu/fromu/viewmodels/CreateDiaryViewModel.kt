package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.PostDiaryBookReq
import com.fromu.fromu.data.remote.network.response.PostDiaryBookRes
import com.fromu.fromu.data.repository.CreateDiaryRepo
import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CreateDiaryViewModel @Inject constructor(private val createDiaryRepo: CreateDiaryRepo) : BaseViewModel() {
    // 선택한 다이어리 커버 아이디
    val selectCoverNum: MutableStateFlow<Int> = MutableStateFlow(0)

    // 사용자가 입력한 다이어리 제목
    val inputDiaryName: MutableStateFlow<String> = MutableStateFlow("")

    // 일기장 이름 유효성 결과
    val isValidDiaryName: MutableStateFlow<Boolean> = MutableStateFlow(false)

    // 다이어리 생성 결과
    val isSuccessCreateDiary: MutableStateFlow<Boolean> = MutableStateFlow(false)

    suspend fun postDiaryBook(): LiveData<Resource<PostDiaryBookRes>> {
        return createDiaryRepo.postDiaryBook(PostDiaryBookReq(selectCoverNum.value, inputDiaryName.value)).asLiveData()
    }
}