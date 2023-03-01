package com.fromu.fromu.utils.sns

import android.content.Context
import com.fromu.fromu.utils.Logger
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient


class KakaoLoginManager(private val context: Context) {

    interface OnKakaoLoginListener {
        fun onSuccess(accessToken: String)
        fun onFailure(errorMsg: String)
    }

    private var kakaoApiClient: UserApiClient = UserApiClient.instance

    /**
     * 카카오 로그인
     *
     * @return Map의 Key는 카카오 로그인 성공 여부, value는 성공 시 accessToken, 실패 시 error 메세지
     */
    fun loginKakao(listener: OnKakaoLoginListener) {
        if (kakaoApiClient.isKakaoTalkLoginAvailable(context)) { //카카오톡이 설치되어 있는 경우 카카오톡으로 로그인
            kakaoApiClient.loginWithKakaoTalk(context, callback = { token, error ->
                setLoginResult(token, error, listener)
            })
        } else { //카카오톡이 설치되어 있지 않은 경우 카카오계정으로 로그인
            kakaoApiClient.loginWithKakaoAccount(context, callback = { token, error ->
                setLoginResult(token, error, listener)
            })
        }
    }

    /**
     * 카카오 로그인 결과를 Map 형식으로 반환하는 메소드
     *
     * @param token
     * @param error
     * @return
     */
    private fun setLoginResult(token: OAuthToken?, error: Throwable?, listener: OnKakaoLoginListener) {
        if (error != null) {
            listener.onFailure(error.message ?: "카카오 계정으로 로그인 실패")
        } else if (token != null) {
            listener.onSuccess(token.accessToken)
        } else {
            listener.onFailure("카카오 계정으로 로그인 실패")
        }
    }


    /**
     * 카카오톡 로그아웃 메소드
     */
    fun logoutKakao() {
        kakaoApiClient.logout { error ->
            if (error != null) {
                Logger.e("kakao", error.message.toString())
            } else {
                Logger.d("kakao", "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
    }

    /**
     * 유저 정보 출력
     */
    fun showUserInfo() {
        kakaoApiClient.me { user, error ->
            if (error != null) {
                Logger.e("kakao", "사용자 정보 요청 실패")
            } else if (user != null) {
                Logger.d(
                    "kakao",
                    "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}" +
                            "\n토큰: ${user.kakaoAccount}"
                )
            }
        }
    }
}