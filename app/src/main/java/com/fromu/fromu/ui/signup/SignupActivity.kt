package com.fromu.fromu.ui.signup

import android.os.Bundle
import androidx.activity.viewModels
import com.fromu.fromu.databinding.ActivitySignupBinding
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.utils.UiUtils
import com.fromu.fromu.viewmodels.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : BaseActivity<ActivitySignupBinding>(ActivitySignupBinding::inflate) {

    companion object {
        const val EMAIL_KEY = "email"
    }

    private val signupViewModel: SignupViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initView()
    }

    private fun initData() {
        intent.getStringExtra(EMAIL_KEY)?.let {
            signupViewModel.email.value = it
        }
    }

    private fun initView() {
        UiUtils.setFullScreenWithStatusBar(this)
    }
}