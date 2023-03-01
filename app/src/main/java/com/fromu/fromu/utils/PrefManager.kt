package com.fromu.fromu.utils

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PrefManager @Inject constructor(val sp: SharedPreferences) {

    companion object {
        const val X_ACCESS_TOKEN = "X-ACCESS-TOKEN"
        const val SHARED_PREFERENCES_NAME = "FROMU_APP"
        const val FCM_TOKEN_KEY = "fcmToken"
        const val PUSH_ALARM_ENABLE_KEY = "isPushAlarmEnable"
        const val LOGIN_TOKEN_KEY = "loginToken"
        const val WHETHER_SHOW_INVITATION_DESCRIPTION = "whetherShowInvitationDescription" //초대장의 디스크립션을 한 번이라도 봤으면 flase, 아니면 true
        const val USER_ID = "userId"
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
    fun getLoginToken(): String {
        return sp.getString(X_ACCESS_TOKEN, "").toString()
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
}