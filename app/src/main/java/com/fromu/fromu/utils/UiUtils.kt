package com.fromu.fromu.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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


        fun getStatusBarHeight(context: Context): Int {
            val screenSizeType: Int = context.getResources().getConfiguration().screenLayout and
                    Configuration.SCREENLAYOUT_SIZE_MASK
            var statusbar = 0
            if (screenSizeType != Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                val resourceId: Int = context.resources.getIdentifier("status_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    statusbar = context.resources.getDimensionPixelSize(resourceId)
                }
            }
            return statusbar
        }


        /**
         * 텍스트를 한 글자 씩 나오게 해주는 메소드
         *
         * @param text 보여줄 텍스트
         * @param tv 텍스트 뷰
         * @param millis 나오는 간격
         */
        fun delayShowText(text: String, tv: TextView, millis: Long) {
            CoroutineScope(Dispatchers.Main).launch {
                for (i in text.indices) {
                    tv.text = text.subSequence(0, i + 1)
                    delay(millis)
                }
            }
        }
    }
}