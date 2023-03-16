package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.PostLetterReq
import com.fromu.fromu.data.remote.network.response.PostLetterRes
import com.fromu.fromu.data.repository.WriteLetterRepo
import com.fromu.fromu.ui.base.BaseViewModel
import com.fromu.fromu.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteLetterViewModel @Inject constructor(private val writeLetterRepo: WriteLetterRepo) : BaseViewModel() {
    // 선택한 우표 id
    val selectStampId: MutableStateFlow<Int> = MutableStateFlow(0)

    // 편지 내용
    val inputContents: MutableStateFlow<String> = MutableStateFlow("")

    // 내가 보낸 편지를 받은 우편함 이름
    val receiveMailBoxName: MutableStateFlow<String> = MutableStateFlow("")

    // 편지 전송 성공 여부
    val isPostSuccess: MutableStateFlow<Boolean> = MutableStateFlow(false)

    // 답장할 편지의 id
    val letterId: MutableStateFlow<Int> = MutableStateFlow(0)


    // postLetter 결과
    private var _postLetterResult: MutableLiveData<Resource<PostLetterRes>> = MutableLiveData()
    val postLetterResult: LiveData<Resource<PostLetterRes>>
        get() = _postLetterResult


    /**
     * 편지 보내기
     */
    fun postLetter() {
        viewModelScope.launch {
            selectStampId.value?.let { selectStampId ->
                writeLetterRepo.postLetter(PostLetterReq(inputContents.value, selectStampId)).collect {
                    _postLetterResult.value = it
                }
            } ?: let {
                Logger.e("postLetter", "stamp's id is null")
            }
        }
    }

    /**
     * 받은 편지 답장 보내기
     */
    fun postReplyLetter() {
        viewModelScope.launch {
            selectStampId.value?.let { selectStampId ->
                writeLetterRepo.postReplyLetter(letterId.value, PostLetterReq(inputContents.value, selectStampId)).collect {
                    _postLetterResult.value = it
                }
            } ?: let {
                Logger.e("postLetter", "stamp's id is null")
            }
        }
    }
}