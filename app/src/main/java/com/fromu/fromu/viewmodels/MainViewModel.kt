package com.fromu.fromu.viewmodels

import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {
    val fromCount: MutableStateFlow<String> = MutableStateFlow("0")
}