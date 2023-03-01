package com.fromu.fromu.ui.signup

import android.os.Bundle
import androidx.activity.viewModels
import com.fromu.fromu.databinding.ActivitySignupBinding
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.utils.UiUtils
import com.fromu.fromu.viewmodels.SignupViewModel

class SignupActivity : BaseActivity<ActivitySignupBinding>(ActivitySignupBinding::inflate) {

    private val signupViewModel: SignupViewModel by viewModels()
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