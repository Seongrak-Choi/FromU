package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.CheckMatchingRes
import com.fromu.fromu.data.remote.network.response.UserInfoRes
import com.fromu.fromu.data.repository.InvitationRepo
import com.fromu.fromu.ui.base.BaseViewModel
import com.fromu.fromu.utils.PrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class InvitationViewModel @Inject constructor(
    private val invitationRepo: InvitationRepo,
    private val prefManager: PrefManager
) : BaseViewModel() {

    // 내 코드
    val myCode: MutableStateFlow<String> = MutableStateFlow("")

    // 내 닉네임
    val myNickname: MutableStateFlow<String> = MutableStateFlow("")

    // Description visible 여부
    val isShowDescription: MutableStateFlow<Boolean> = MutableStateFlow(false)

    // 매칭 여부
    private var _isMatching: MutableLiveData<Resource<CheckMatchingRes>> = MutableLiveData()
    val isMatching: LiveData<Resource<CheckMatchingRes>>
        get() = _isMatching


    // 로그인한 유저 정보
    private var _userInfo: MutableLiveData<Resource<UserInfoRes>> = MutableLiveData()
    val userInfo: LiveData<Resource<UserInfoRes>>
        get() = _userInfo


    /**
     * 디스크립션 visible 셋팅
     */
    fun setWhetherFirstDescription() {
        isShowDescription.value = prefManager.sp.getBoolean(PrefManager.WHETHER_SHOW_INVITATION_DESCRIPTION, true)
    }

    /**
     * 유저 정보 조회
     *
     * @param userId : Int = 조회할 유저 아이디
     */
    suspend fun getUserInfo(userId: Int) {
        invitationRepo.getUserInfo(userId).collect {

            _userInfo.value = it
        }
    }

    /**
     * 매칭 여부 조회
     */
    suspend fun getCheckMatching() {
        invitationRepo.getCheckMatching().collect {
            _isMatching.value = it
        }
    }
}