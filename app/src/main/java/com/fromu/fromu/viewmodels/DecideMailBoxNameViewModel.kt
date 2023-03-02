package com.fromu.fromu.viewmodels

import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DecideMailBoxNameViewModel @Inject constructor() : BaseViewModel() {

    val isInvalidMailBoxName: MutableStateFlow<Boolean> = MutableStateFlow(false)
}