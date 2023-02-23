package com.fromu.fromu.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.fromu.fromu.base.BaseActivity
import com.fromu.fromu.databinding.ActivitySplashBinding
import com.fromu.fromu.utils.UiUtils

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        initData()
        initView()
    }

    private fun initData() {
        handleDynamicLinks()
    }

    private fun initView() {
        UiUtils.hideStatusBarAndSystemBar(window)
        UiUtils.setFullScreenWithStatusBar(this)

        binding.apply {
            lifecycleOwner = this@SplashActivity
        }
    }



    /**
     * dynamic link handle method
     */
    private fun handleDynamicLinks() {
        //TODO 추후 다이나믹 링크 연동
    }
}