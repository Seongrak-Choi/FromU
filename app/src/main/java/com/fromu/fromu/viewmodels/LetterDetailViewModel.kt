package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fromu.fromu.data.dto.ReadLetterResult
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.ReadLetterRes
import com.fromu.fromu.data.repository.MailBoxRepo
import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LetterDetailViewModel @Inject constructor(private val mailBoxRepo: MailBoxRepo) : BaseViewModel() {

    val letterDetail: MutableLiveData<ReadLetterResult> = MutableLiveData()
    val readFlag: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var _readLetterResult: MutableLiveData<Resource<ReadLetterRes>> = MutableLiveData()
    val readLetterResult: LiveData<Resource<ReadLetterRes>>
        get() = _readLetterResult

    fun readLetter(letterId: Int) {
        viewModelScope.launch {
            mailBoxRepo.readLetter(letterId).collect {
                _readLetterResult.value = it
            }
        }
    }
}