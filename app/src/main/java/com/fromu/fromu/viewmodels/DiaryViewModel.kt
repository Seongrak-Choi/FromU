package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.DiaryViewRes
import com.fromu.fromu.data.remote.network.response.PatchDiaryBooksPassRes
import com.fromu.fromu.data.repository.DiaryViewRepo
import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(private val diaryViewRepo: DiaryViewRepo) : BaseViewModel() {
    val myNickname: MutableStateFlow<String> = MutableStateFlow("")
    val partnerNickname: MutableStateFlow<String> = MutableStateFlow("")

    // 디데이
    val dday: MutableStateFlow<String> = MutableStateFlow("00")

    // 다이어리 이름
    val diaryName: MutableStateFlow<String> = MutableStateFlow("추억기록장")

    // 다이어리 커버 이미지 id
    val diaryCoverNum: MutableStateFlow<Int> = MutableStateFlow(0)

    // 디데이 설정 유무
    val isSetFirstMetDay: MutableStateFlow<Boolean> = MutableStateFlow(false)

    // 일기장 상태 코드
    val diaryBookStatusId: MutableStateFlow<String> = MutableStateFlow("0")


    // 다이어리 뷰 결과
    private var _diaryViewInfo: MutableLiveData<Resource<DiaryViewRes>> = MutableLiveData()
    val diaryViewInfo: LiveData<Resource<DiaryViewRes>>
        get() = _diaryViewInfo


    // 일기장 보내기 결과
    private var _diaryPassResult: MutableLiveData<Resource<PatchDiaryBooksPassRes>> = MutableLiveData()
    val diaryPassResult: LiveData<Resource<PatchDiaryBooksPassRes>>
        get() = _diaryPassResult

    /**
     * 다이어리 뷰 데이터 조회
     */
    fun getDiaryView() {
        viewModelScope.launch {
            diaryViewRepo.getDiaryView().collect {
                _diaryViewInfo.value = it
            }
        }
    }


    /**
     * 일기장 보내기
     */
    fun patchDiaryBooksPass() {
        viewModelScope.launch {
            diaryViewRepo.patchDiaryBooksPass().collect {
                _diaryPassResult.value = it
            }
        }
    }
}