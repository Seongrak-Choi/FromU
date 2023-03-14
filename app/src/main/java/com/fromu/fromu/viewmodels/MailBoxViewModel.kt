package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.MailBoxViewRes
import com.fromu.fromu.data.repository.MailBoxRepo
import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MailBoxViewModel @Inject constructor(private val mailBoxRepo: MailBoxRepo) : BaseViewModel() {
    val mailBoxName: MutableStateFlow<String> = MutableStateFlow("")
    val newLetterId: MutableStateFlow<Int> = MutableStateFlow(0)

    private var _mailBoxViewResult: MutableLiveData<Resource<MailBoxViewRes>> = MutableLiveData()
    val mailBoxViewResult: LiveData<Resource<MailBoxViewRes>>
        get() = _mailBoxViewResult


    /**
     * 우편함 화면 조회 api
     *
     */
    fun getMailBoxView() {
        viewModelScope.launch {
            mailBoxRepo.getMailBoxView().collect {
                _mailBoxViewResult.value = it
            }
        }
    }
}