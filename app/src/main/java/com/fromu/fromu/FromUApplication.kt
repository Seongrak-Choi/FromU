package com.fromu.fromu

import android.app.Application
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.UiUtils
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class FromUApplication : Application() {

    companion object {
        // statusbar 높이
        var statusHeight: Int = 50
    }

    override fun onCreate() {
        super.onCreate()

        // Timber Tree 인스턴스 설정
        Timber.plant(Timber.DebugTree())

        //카카오 로그인
        KakaoSdk.init(this, Const.KAKAO_SDK_NATIVE_APP_KEY)

        statusHeight = UiUtils.getStatusBarHeight(this)
    }
}