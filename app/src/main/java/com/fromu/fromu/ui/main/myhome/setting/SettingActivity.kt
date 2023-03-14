package com.fromu.fromu.ui.main.myhome.setting

import android.os.Bundle
import com.fromu.fromu.databinding.ActivitySettingBinding
import com.fromu.fromu.ui.base.BaseActivity

class SettingActivity : BaseActivity<ActivitySettingBinding>(ActivitySettingBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initView()
    }

    private fun initData() {}
    private fun initView() {

    }
}