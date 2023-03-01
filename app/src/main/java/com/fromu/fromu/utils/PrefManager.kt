package com.fromu.fromu.utils

import android.content.SharedPreferences
import javax.inject.Inject


class PrefManager @Inject constructor(private val sp: SharedPreferences) {

    companion object {
        const val X_ACCESS_TOKEN = "X-ACCESS-TOKEN"
        const val SHARED_PREFERENCES_NAME = "FROMU_APP"
        const val FCM_TOKEN_KEY = "fcmToken"
        const val PUSH_ALARM_ENABLE_KEY = "isPushAlarmEnable"
        const val LOGIN_TOKEN_KEY = "loginToken"
    }

    private val editor = sp.edit()

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
    fun setLoginToken(loginToken: String) {
        editor.putString(LOGIN_TOKEN_KEY, loginToken).apply()
    }

    /**
     * 로그인 토큰 반환
     *
     * @return
     */
    fun getLoginToken(): String {
        return sp.getString(LOGIN_TOKEN_KEY, "").toString()
    }
}