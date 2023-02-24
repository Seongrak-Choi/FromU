package com.fromu.fromu.ui.signup

import android.os.Bundle
import com.fromu.fromu.databinding.ActivitySignupBinding
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.utils.UiUtils

class SignupActivity : BaseActivity<ActivitySignupBinding>(ActivitySignupBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initView()
    }

    private fun initData() {}
    private fun initView() {
        UiUtils.setFullScreenWithStatusBar(this)
    }
}