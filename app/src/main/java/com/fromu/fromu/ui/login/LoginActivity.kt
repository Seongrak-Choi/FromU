package com.fromu.fromu.ui.login

import android.os.Bundle
import com.fromu.fromu.databinding.ActivityLoginBinding
import com.fromu.fromu.ui.base.BaseActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initView()
    }

    private fun initData() {}
    private fun initView() {}

}