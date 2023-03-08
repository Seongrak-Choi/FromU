package com.fromu.fromu

import android.app.Application
import com.fromu.fromu.model.AppRunningState
import com.fromu.fromu.utils.AppLifecycle
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.PrefManager
import com.fromu.fromu.utils.UiUtils
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FromUApplication : Application() {

    companion object {
        var RUNNING_FLAG: AppRunningState = AppRunningState.NOT_RUNNING

        // statusbar 높이
        var statusHeight: Int = 0

        // PrefManager 싱글톤으로 사용
        lateinit var prefManager: PrefManager
    }

    override fun onCreate() {
        super.onCreate()
        prefManager = PrefManager(applicationContext.getSharedPreferences(PrefManager.SHARED_PREFERENCES_NAME, MODE_PRIVATE))

        //카카오 로그인
        KakaoSdk.init(this, Const.KAKAO_SDK_NATIVE_APP_KEY)

        statusHeight = UiUtils.getStatusBarHeight(this)

        registerLifecycle()
    }

    /**
     * Activity들의 생명주기 콜백 셋팅
     */
    private fun registerLifecycle() {
        registerActivityLifecycleCallbacks(AppLifecycle())
    }
}