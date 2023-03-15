package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.MailListRes
import com.fromu.fromu.data.repository.MailListRepo
import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MailListViewModel @Inject constructor(private val mailListRepo: MailListRepo) : BaseViewModel() {

    // 메일이 있는지 없는지
    val isExistSendMail: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isExistReceiveMail: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var _getSendMailListResult: MutableLiveData<Resource<MailListRes>> = MutableLiveData()
    val getSendMailListResult: LiveData<Resource<MailListRes>>
        get() = _getSendMailListResult

    private var _getReceiveMailListResult: MutableLiveData<Resource<MailListRes>> = MutableLiveData()
    val getReceiveMailListResult: LiveData<Resource<MailListRes>>
        get() = _getReceiveMailListResult


    fun getSendMailList() {
        viewModelScope.launch {
            mailListRepo.getSendMailList().collect {
                _getSendMailListResult.value = it
            }
        }
    }


    fun getReceiveMailList() {
        viewModelScope.launch {
            mailListRepo.getReceiveMailList().collect {
                _getReceiveMailListResult.value = it
            }
        }
    }
}