package com.fromu.fromu.utils

class Const {
    companion object {
        const val SHARED_PREFERENCES_NAME = "FROMU_APP"

          /* 카카오 SDK 네이티브 앱 키 */
        const val KAKAO_SDK_NATIVE_APP_KEY = "c36fb0ce439b3a9e9be9673d048dadb4"

        /* 구글 클라이언트 아이디 */
        const val GOOGLE_CLIENT_ID = "662521080151-229e0lg2re6o17g8m1gnocape34i8m9v.apps.googleusercontent.com"
    }

    enum class LoginType {
        JWT, KAKAO, GOOGLE
    }
}