package com.fromu.fromu.utils

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class UiUtils {
    companion object {


        /**
         * 투명한 상단 statusbar가 포함된 풀스크린 모드를 액티비티에 적용하기 위한 메소드
         *
         * 안드로이드 킷캣(KITKAT, API 19) 이상에서 적용 가능한 로직이지만,
         * 해당 프로젝트의 minSDK는 28이기 때문에 상관없습니다.
         *
         * @param activity
         */
        @JvmStatic
        @SuppressLint("ObsoleteSdkInt")
        fun setFullScreenWithStatusBar(activity: AppCompatActivity) {
            activity.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    @Suppress("DEPRECATION")
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                }
            }
        }

        /**
         * 상단의 statusBar와 하단의 SystemBar 숨기는 메소드
         *
         */
        fun hideStatusBarAndSystemBar(window: Window) {
            WindowCompat.getInsetsController(window, window.decorView).apply {
                isAppearanceLightStatusBars = true
                hide(WindowInsetsCompat.Type.systemBars()) // systemBar 숨기기
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE // 스와이프로 가려진 systemBar 볼 수 있음
            }
        }
    }
}