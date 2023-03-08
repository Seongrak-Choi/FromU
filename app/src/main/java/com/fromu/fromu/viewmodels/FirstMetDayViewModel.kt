package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.PatchFirstMetDayReq
import com.fromu.fromu.data.remote.network.response.PatchFirstMetDayRes
import com.fromu.fromu.data.repository.FirstMetDayRepo
import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class FirstMetDayViewModel @Inject constructor(private val firstMetDayRepo: FirstMetDayRepo) : BaseViewModel() {

    // 처음 만난 날 값 ex) 19960210
    val firstMetDay: MutableStateFlow<String> = MutableStateFlow("")

    // 처음 만난 날 유효성 확인 결과
    val isValidFirstMetDay: MutableStateFlow<Boolean> = MutableStateFlow(false)

    suspend fun patchFirstMetDay(): LiveData<Resource<PatchFirstMetDayRes>> {
        return firstMetDayRepo.patchFirstMetDay(PatchFirstMetDayReq(firstMetDay.value)).asLiveData()
    }
}