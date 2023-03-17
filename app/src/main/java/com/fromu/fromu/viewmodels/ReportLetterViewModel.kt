package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.ReportLetterReq
import com.fromu.fromu.data.remote.network.response.ReportLetterRes
import com.fromu.fromu.data.repository.ReportLetterRepo
import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ReportLetterViewModel @Inject constructor(private val reportLetterRepo: ReportLetterRepo) : BaseViewModel() {
    val letterId: MutableStateFlow<Int> = MutableStateFlow(0)
    val contents: MutableStateFlow<String> = MutableStateFlow("")


    fun reportLetter(): LiveData<Resource<ReportLetterRes>> {
        return reportLetterRepo.reportLetter(letterId.value, ReportLetterReq(contents.value)).asLiveData()
    }
}