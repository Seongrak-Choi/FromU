package com.fromu.fromu.utils

class Const {
    companion object {
        /* BASE_URL */
        const val BASE_URL = "https://fromu.store/app/"

        /* api 서버 응답 코드 */
        const val SUCCESS_CODE = 1000

        /* 카카오 SDK 네이티브 앱 키 */
        const val KAKAO_SDK_NATIVE_APP_KEY = "c36fb0ce439b3a9e9be9673d048dadb4"

        /* 구글 클라이언트 아이디 */
        const val GOOGLE_CLIENT_ID = "662521080151-229e0lg2re6o17g8m1gnocape34i8m9v.apps.googleusercontent.com"

        // 특수문자, 공백 검사용 정규식
        const val NO_SPECIAL_CHAR_AND_NO_GAP_EXPRESSION = "^[a-zA-Z가-힣0-9]*$"

        const val NO_GAP_EXPRESSION = "^\\S*$"

        // 초대 코드 길이
        const val INVITATION_CODE_LENGTH = 8

        // dday 설정 X
        const val DDAY_NO_SETTING = 0
    }
}