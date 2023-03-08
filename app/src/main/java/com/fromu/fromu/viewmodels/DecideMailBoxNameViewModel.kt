package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.PatchMailBoxNameReq
import com.fromu.fromu.data.remote.network.response.PatchMailBoxNameRes
import com.fromu.fromu.data.repository.CoupleRepo
import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DecideMailBoxNameViewModel @Inject constructor(private val coupleRepo: CoupleRepo) : BaseViewModel() {
    val isInvalidMailBoxName: MutableStateFlow<Boolean> = MutableStateFlow(false)

    suspend fun patchMailBoxName(patchMailBoxNameReq: PatchMailBoxNameReq): LiveData<Resource<PatchMailBoxNameRes>> {
        return coupleRepo.patchMailBoxName(patchMailBoxNameReq).asLiveData()
    }
}