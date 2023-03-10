package com.fromu.fromu.utils

import android.content.SharedPreferences

class PrefManager constructor(val sp: SharedPreferences) {

    companion object {
        const val X_ACCESS_TOKEN = "X-ACCESS-TOKEN"
        const val REFRESH_TOKEN = "refreshToken"
        const val SHARED_PREFERENCES_NAME = "FROMU_APP"
        const val FCM_TOKEN_KEY = "fcmToken"
        const val PUSH_ALARM_ENABLE_KEY = "isPushAlarmEnable"
        const val LOGIN_TOKEN_KEY = "loginToken"
        const val WHETHER_SHOW_INVITATION_DESCRIPTION = "whetherShowInvitationDescription" //초대장의 디스크립션을 한 번이라도 봤으면 flase, 아니면 true
        const val USER_ID = "userId"
        const val USER_LOGIN_EMAIL = "userLoginEmail"
        const val INIT_ON_BOARDING_KEY = "initOnBoardingKey" // 온보딩 봤으면 true 안 봤으면 false
        const val WHETHER_SHOW_DIARY_DESCRIPTION = "whetherShowDiaryDescription"
    }

    val editor = sp.edit()

    fun clearAll() {
        editor.clear()
        editor.apply()
    }

    /**
     * FCM ID 반환
     *
     * @return
     */
    fun getFcmId(): String {
        return sp.getString(FCM_TOKEN_KEY, "") ?: ""
    }

    /**
     * FCM ID 저장
     *
     * @param fcmId
     */
    fun setFcmId(fcmId: String) {
        editor.putString(FCM_TOKEN_KEY, fcmId).apply()
    }

    /**
     * 푸시 알림 설정값 저장
     *
     * @param value
     */
    fun setPushAlarmEnable(value: Boolean) {
        editor.putBoolean(PUSH_ALARM_ENABLE_KEY, value).apply()
    }

    /**
     * 푸시 알림 설정값 반환
     *
     * @return
     */
    fun isPushAlarmEnable(): Boolean {
        return sp.getBoolean(PUSH_ALARM_ENABLE_KEY, true)
    }

    /**
     * 로그인 토큰 저장
     *
     * @param loginToken
     */
    fun setLoginToken(loginToken: String?) {
        loginToken?.let { jwt ->
            editor.putString(X_ACCESS_TOKEN, jwt).apply()
        }
    }

    /**
     * 로그인 토큰 반환
     *
     * @return
     */
    fun getLoginToken(): String? {
        return sp.getString(X_ACCESS_TOKEN, null)
    }

    /**
     * 리프레시 토큰 저장
     *
     * @param refreshToken
     */
    fun setRefreshToken(refreshToken: String) {
        editor.putString(REFRESH_TOKEN, refreshToken).apply()
    }

    /**
     * 리프레쉬 토큰 반환
     *
     * @return
     */
    fun getRefreshToken(): String? {
        return sp.getString(REFRESH_TOKEN, "")
    }

    /**
     * UserId 저장
     *
     * @param userId
     */
    fun setUserId(userId: Int?) {
        userId?.let {
            sp.edit().putInt(USER_ID, it).apply()
        }
    }

    /**
     * UserId
     *
     * @return
     */
    fun getUserId(): Int? {
        sp.getInt(USER_ID, -1).apply {
            return if (this == -1) null
            else this
        }
    }

    /**
     * 유저가 로그인한 이메일 저장
     *
     * @param userId
     */
    fun setUserLoginEmail(email: String?) {
        email?.let {
            sp.edit().putString(USER_LOGIN_EMAIL, it).apply()
        }
    }

    /**
     * 유저가 로그인한 이메일 반환
     *
     * @return
     */
    fun getUserLoginEmail(): String? {
        sp.getString(USER_LOGIN_EMAIL, null).apply {
            return this
        }
    }
}