package com.fromu.fromu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.fromu.fromu.FromUApplication
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.PatchFcmTokenReq
import com.fromu.fromu.data.remote.network.request.SignupReq
import com.fromu.fromu.data.remote.network.response.SignupRes
import com.fromu.fromu.data.repository.LoginRepo
import com.fromu.fromu.data.repository.SignupRepo
import com.fromu.fromu.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupRepo: SignupRepo,
    private val loginRepo: LoginRepo
) : BaseViewModel() {
    // 로그인 했던 이메일
    val email: MutableStateFlow<String> = MutableStateFlow("")

    // 입력한 nickName 값
    val nickname: MutableStateFlow<String> = MutableStateFlow("")

    // nickName의 유효성 확인결과
    val isValidNickname: MutableStateFlow<Boolean> = MutableStateFlow(false)

    // 입력한 생년월일 값 ex) 19960210
    val birthday: MutableStateFlow<String> = MutableStateFlow("")

    // 생년월일 유효성 확인 결과
    val isValidBirthday: MutableStateFlow<Boolean> = MutableStateFlow(false)

    // 선택한 Gender 값
    val gender: MutableStateFlow<String> = MutableStateFlow("")

    // 이용약관 체크 상태
    val isCheckedTermsOfUser: MutableStateFlow<Boolean> = MutableStateFlow(false)

    // 개인 정보 수집 체크 상태
    val isCheckedPersonalInformation: MutableStateFlow<Boolean> = MutableStateFlow(false)


    fun setGender(value: String) {
        gender.value = value
    }

    fun clickTermsOfUser() {
        isCheckedTermsOfUser.value = !isCheckedTermsOfUser.value
    }

    fun clickPersonalInformation() {
        isCheckedPersonalInformation.value = !isCheckedPersonalInformation.value
    }

    /**
     * 사용자가 입력한 데이터 SignupReq형으로 반환
     *
     * @return : SignupReq
     */
    private fun getSignupReq(): SignupReq {
        return SignupReq(email.value, nickname.value, birthday.value, gender.value)
    }

    /**
     * 회원가입 api 호출
     *
     * @return
     */
    suspend fun postSignup(): LiveData<Resource<SignupRes>> {
        return signupRepo.postSignup(getSignupReq()).map { resource ->
            if (resource is Resource.Success) {
                resource.body.result.apply {
                    FromUApplication.prefManager.setLoginData(userId, jwt, refreshToken)

                    patchFcmToken(jwt)
                }
            }
            resource
        }.asLiveData()
    }

    private fun patchFcmToken(jwt: String) {
        loginRepo.patchFcmToken(jwt, PatchFcmTokenReq(FromUApplication.prefManager.getFcmId()))
    }
}