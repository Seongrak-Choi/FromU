package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.AddSchedulesReq
import com.fromu.fromu.data.remote.network.request.PatchSchedulesReq
import com.fromu.fromu.data.remote.network.response.AddSchedulesRes
import com.fromu.fromu.data.remote.network.response.DeleteSchedulesRes
import com.fromu.fromu.data.remote.network.response.GetDetailScheduleRes
import com.fromu.fromu.data.remote.network.response.PatchSchedulesRes
import com.fromu.fromu.data.repository.CalendarRepo
import com.fromu.fromu.ui.base.BaseViewModel
import com.fromu.fromu.ui.dialog.DialogBottomSchedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DialogDetailScheduleViewModel @Inject constructor(private val calendarRepo: CalendarRepo) : BaseViewModel() {

    // detail_schedule에서 로딩 상태 저장할 변수
    val isShowDialogLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    // 특정 일에 일정이 있는 지
    val isExistSchedule: MutableStateFlow<Boolean> = MutableStateFlow(true)

    // 목록인지 일정 입력 화면인지 상태 저장할 변수
    val uiType: MutableStateFlow<DialogBottomSchedule.DetailScheduleUiType> = MutableStateFlow(DialogBottomSchedule.DetailScheduleUiType.INDEX)

    // 일정 수정 or 추가 할 때 입력하는 EditText의 값을 저장
    val scheduleInputData: MutableStateFlow<String> = MutableStateFlow("")

    // UiType이 Input일 때 수정인지 추가인지 상태 저장할 변수
    val inputType: MutableStateFlow<DialogBottomSchedule.DetailScheduleInputType> = MutableStateFlow(DialogBottomSchedule.DetailScheduleInputType.ADD)


    // 상세 일정 조회 결과
    private var _getDetailScheduleResult: MutableLiveData<Resource<GetDetailScheduleRes>> = MutableLiveData()
    val getDetailScheduleResult: LiveData<Resource<GetDetailScheduleRes>>
        get() = _getDetailScheduleResult

    // 일정 등록 결과
    private var _addScheduleResult: MutableLiveData<Resource<AddSchedulesRes>> = MutableLiveData()
    val addScheduleResult: LiveData<Resource<AddSchedulesRes>>
        get() = _addScheduleResult

    // 일정 삭제 결과
    private var _deleteScheduleResult: MutableLiveData<Resource<DeleteSchedulesRes>> = MutableLiveData()
    val deleteScheduleResult: LiveData<Resource<DeleteSchedulesRes>>
        get() = _deleteScheduleResult

    // 일정 수정 결과
    private var _patchScheduleResult: MutableLiveData<Resource<PatchSchedulesRes>> = MutableLiveData()
    val patchScheduleResult: LiveData<Resource<PatchSchedulesRes>>
        get() = _patchScheduleResult

    /**
     * 특정 일의 일정 상세 정보를 조회
     *
     * @param month
     */
    fun getDetailSchedule(month: String, day: String) {
        viewModelScope.launch {
            calendarRepo.getDetailSchedule(month, day).collect {
                _getDetailScheduleResult.value = it
            }
        }
    }

    /**
     * 일정 등록
     *
     * @param addSchedulesReq
     */
    fun addSchedules(addSchedulesReq: AddSchedulesReq) {
        viewModelScope.launch {
            calendarRepo.addSchedules(addSchedulesReq).collect {
                _addScheduleResult.value = it
            }
        }
    }

    /**
     * 일정 삭제
     *
     * @param scheduleId
     */
    fun deleteSchedule(scheduleId: Int) {
        viewModelScope.launch {
            calendarRepo.deleteSchedules(scheduleId).collect {
                _deleteScheduleResult.value = it
            }
        }
    }

    /**
     * 일정 수정
     *
     * @param scheduleId
     * @param patchSchedulesReq
     */
    fun patchSchedules(scheduleId: Int, patchSchedulesReq: PatchSchedulesReq) {
        viewModelScope.launch {
            calendarRepo.patchSchedules(scheduleId, patchSchedulesReq).collect {
                _patchScheduleResult.value = it
            }
        }
    }
}