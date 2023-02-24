package com.fromu.fromu.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fromu.fromu.databinding.ActivitySplashBinding
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.utils.UiUtils
import com.fromu.fromu.viewmodels.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private val splashViewModel: SplashViewModel by viewModels()

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

        binding.apply {}
    }


    /**
     * dynamic link handle method
     */
    private fun handleDynamicLinks() {
        //TODO 추후 다이나믹 링크 연동
    }
}