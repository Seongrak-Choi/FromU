package com.fromu.fromu

import android.app.Application
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.UiUtils
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FromUApplication : Application() {

    companion object {
        // statusbar 높이
        var statusHeight: Int = 50
    }

    override fun onCreate() {
        super.onCreate()

        //카카오 로그인
        KakaoSdk.init(this, Const.KAKAO_SDK_NATIVE_APP_KEY)

        statusHeight = UiUtils.getStatusBarHeight(this)
    }
}