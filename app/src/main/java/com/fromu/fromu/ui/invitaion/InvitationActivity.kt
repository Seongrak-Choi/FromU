package com.fromu.fromu.ui.invitaion

import android.os.Bundle
import com.fromu.fromu.databinding.ActivityInvitationBinding
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.utils.UiUtils

class InvitationActivity : BaseActivity<ActivityInvitationBinding>(ActivityInvitationBinding::inflate) {
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