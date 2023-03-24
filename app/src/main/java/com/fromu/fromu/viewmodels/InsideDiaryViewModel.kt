package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.AllDiariesRes
import com.fromu.fromu.data.remote.network.response.ChangeFirstPageImgRes
import com.fromu.fromu.data.remote.network.response.DeleteDiaryRes
import com.fromu.fromu.data.remote.network.response.FirstPageRes
import com.fromu.fromu.data.repository.InsideDiaryRepo
import com.fromu.fromu.model.listener.DetailDiaryListener
import com.fromu.fromu.ui.base.BaseViewModel
import com.fromu.fromu.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class InsideDiaryViewModel @Inject constructor(private val insideDiaryRepo: InsideDiaryRepo) : BaseViewModel() {

    // 현지 일기 포지션
    val currentDiaryPosition: MutableStateFlow<Int> = MutableStateFlow(0)

    // 일기 전체 개수
    val maxLengthOfDiaries: MutableStateFlow<Int> = MutableStateFlow(0)

    // 일기 첫 장 대표 이미지
    val diaryFirstPageFilePath: MutableStateFlow<String?> = MutableStateFlow(null)

    // 디스크립션 노출 된 적 있는지 확인 / true = 디스크립션 노출, false = 미노출
    val isShowDescription: MutableStateFlow<Boolean> = MutableStateFlow(false)

    // 두 번째 디스크립션 visible 여부
    val isShowDescription2: MutableStateFlow<Boolean> = MutableStateFlow(false)

    // 일기장 내지 첫장 조회 api
    private var _firstPageResult: MutableLiveData<Event<Resource<FirstPageRes>>> = MutableLiveData()
    val firstPageResult: LiveData<Event<Resource<FirstPageRes>>>
        get() = _firstPageResult

    // 일기 첫 장 대표 이미지 변경 결과
    private var _changeFirstPageImgResult: MutableLiveData<Resource<ChangeFirstPageImgRes>> = MutableLiveData()
    val changeFirstPageImgResult: LiveData<Resource<ChangeFirstPageImgRes>>
        get() = _changeFirstPageImgResult

    // 모든 일기의 id 조회 결과
    private var _allDiariesId: MutableLiveData<Event<Resource<AllDiariesRes>>> = MutableLiveData()
    val allDiariesRes: LiveData<Event<Resource<AllDiariesRes>>>
        get() = _allDiariesId

    // 일기 삭제 결과
    private var _deleteDiaryResult: MutableLiveData<Resource<DeleteDiaryRes>> = MutableLiveData()
    val deleteDiaryResult: LiveData<Resource<DeleteDiaryRes>>
        get() = _deleteDiaryResult


    /**
     * 일기 첫 페이지 조회
     */
    fun getFirstPage() {
        viewModelScope.launch {
            insideDiaryRepo.getFirstPage().collect {
                _firstPageResult.value = Event(it)
            }
        }
    }


    /**
     * 모든 일기를 오름차순으로 조회
     *
     * @param diaryBookId
     */
    fun getAllDiaries(diaryBookId: Int) {
        viewModelScope.launch {
            insideDiaryRepo.getAllDiaries(diaryBookId).collect {
                _allDiariesId.value = Event(it)
            }
        }
    }

    /**
     * diaryid로 일기 상세 조회
     */
    fun getDetailDiaries(diaryId: Int, listener: DetailDiaryListener) {
        insideDiaryRepo.getDetailDiariesById(diaryId, listener)
    }

    /**
     * 일기 첫 장 표지 이미지 변경
     */
    fun changeFirstPageImg(imagePath: String?) {
        viewModelScope.launch {
            insideDiaryRepo.changeFirstPageImg(makeFirstPageImgToMultipartBody(imagePath)).collect {
                _changeFirstPageImgResult.value = it
            }
        }
    }

    /**
     * 일기 삭제
     *
     * @param diaryId
     */
    fun deleteDiary(diaryId: Int) {
        viewModelScope.launch {
            insideDiaryRepo.deleteDiary(diaryId).collect {
                _deleteDiaryResult.value = it
            }
        }
    }


    /**
     * make 일기 첫 장 표지 이미지 TO MultiPartBody
     *
     * @return
     */
    private fun makeFirstPageImgToMultipartBody(imagePath: String?): MultipartBody.Part {
        val file = File(imagePath ?: "")
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("imageFile", file.name, requestBody)
    }
}