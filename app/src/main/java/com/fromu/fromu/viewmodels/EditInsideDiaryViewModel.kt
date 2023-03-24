package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.WriteDiaryReq
import com.fromu.fromu.data.remote.network.response.DetailDiaryRes
import com.fromu.fromu.data.remote.network.response.EditDiaryRes
import com.fromu.fromu.data.repository.InsideDiaryRepo
import com.fromu.fromu.ui.base.BaseViewModel
import com.fromu.fromu.utils.Event
import com.fromu.fromu.utils.Utils
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class EditInsideDiaryViewModel @Inject constructor(private val insideDiaryRepo: InsideDiaryRepo) : BaseViewModel() {
    // 감정날씨 선택 값
    val checkWeatherValue: MutableStateFlow<String> = MutableStateFlow("")

    // 자른 이미지 file path
    val cropImgPath: MutableStateFlow<String> = MutableStateFlow("")

    // 입력한 일기 내용
    val inputContents: MutableStateFlow<String> = MutableStateFlow("")
    val isInvalidInputData: MutableStateFlow<Boolean> = MutableStateFlow(false)

    // 일기 작성 날짜
    val writeDate: MutableStateFlow<String?> = MutableStateFlow("")

    // 일기 작성 요일
    val writeDayOfWeek: MutableStateFlow<Int> = MutableStateFlow(0)


    // 일기 상세 정보 조회 결과
    private var _getDetailDiaryResult: MutableLiveData<Event<Resource<DetailDiaryRes>>> = MutableLiveData()
    val getDetailDiaryResult: LiveData<Event<Resource<DetailDiaryRes>>>
        get() = _getDetailDiaryResult

    // 일기 수정 결과
    private var _editDiaryResult: MutableLiveData<Resource<EditDiaryRes>> = MutableLiveData()
    val editDiaryResult: LiveData<Resource<EditDiaryRes>>
        get() = _editDiaryResult


    fun checkWeatherValue(weatherValue: String) {
        checkWeatherValue.value = weatherValue
    }

    /**
     * 감정날씨, 내용을 모두 입력했는지 확인하는 메소드
     *
     */
    fun checkInvalidInputData() {
        isInvalidInputData.value = checkWeatherValue.value.isNotEmpty() && inputContents.value.isNotEmpty()
    }

    /**
     * 다이어리의 상세 정보 조회
     *
     * @param diaryId
     */
    fun getDetailDiariesById(diaryId: Int) {
        viewModelScope.launch {
            insideDiaryRepo.getDetailDiariesById(diaryId).collect {
                _getDetailDiaryResult.value = Event(it)
            }
        }
    }


    /**
     * 일기 수정
     *
     * @param diaryId
     */
    fun editDiary(diaryId: Int) {
        viewModelScope.launch {
            insideDiaryRepo.editDiary(diaryId, Utils.makeSingleImgToMultipartBody(cropImgPath.value), makePatchDiaryRequestBody()).collect {
                _editDiaryResult.value = it
            }
        }
    }


    private fun makePatchDiaryRequestBody(): RequestBody {
        return Gson().toJson(WriteDiaryReq(inputContents.value, checkWeatherValue.value)).toRequestBody("application/json".toMediaTypeOrNull())
    }
}