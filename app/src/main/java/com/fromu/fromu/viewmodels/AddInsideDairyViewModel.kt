package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.WriteDiaryReq
import com.fromu.fromu.data.remote.network.response.WriteDiaryRes
import com.fromu.fromu.data.repository.AddInsideDiaryRepo
import com.fromu.fromu.ui.base.BaseViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddInsideDairyViewModel @Inject constructor(private val addInsideDiaryRepo: AddInsideDiaryRepo) : BaseViewModel() {
    // 감정날씨 선택 값
    val checkWeatherValue: MutableStateFlow<String> = MutableStateFlow("")

    // 자른 이미지 file path
    val cropImgPath: MutableStateFlow<String> = MutableStateFlow("")

    // 입력한 일기 내용
    val inputContents: MutableStateFlow<String> = MutableStateFlow("")
    val isInvalidInputData: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val dayOfToday: MutableStateFlow<String> = MutableStateFlow("")
    val monthOfToday: MutableStateFlow<String> = MutableStateFlow("")
    val dayOfWeek: MutableStateFlow<String> = MutableStateFlow("")

    fun checkWeatherValue(weatherValue: String) {
        checkWeatherValue.value = weatherValue
    }

    /**
     * 감정날씨, 사진, 내용을 모두 입력했는지 확인하는 메소드
     *
     */
    fun checkInvalidInputData() {
        isInvalidInputData.value = checkWeatherValue.value.isNotEmpty() && cropImgPath.value.isNotEmpty() && inputContents.value.isNotEmpty()
    }

    /**
     * 일기 등록
     *
     * @return
     */
    suspend fun writeDiary(): LiveData<Resource<WriteDiaryRes>> {
        return addInsideDiaryRepo.writeDiary(makeImgToMultipartBody(), makePostDiaryRequestBody()).asLiveData()
    }


    private fun makeImgToMultipartBody(): MultipartBody.Part {
        val file = File(cropImgPath.value)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("imageFile", file.name, requestBody)
    }

    private fun makePostDiaryRequestBody(): RequestBody {
        return Gson().toJson(WriteDiaryReq(inputContents.value, checkWeatherValue.value)).toRequestBody("application/json".toMediaTypeOrNull())
    }
}