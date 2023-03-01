package com.fromu.fromu.ui.invitaion

import android.os.Bundle
import androidx.activity.viewModels
import com.fromu.fromu.databinding.ActivityInvitationBinding
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.utils.UiUtils
import com.fromu.fromu.viewmodels.InvitationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvitationActivity : BaseActivity<ActivityInvitationBinding>(ActivityInvitationBinding::inflate) {
    private val invitationViewModel: InvitationViewModel by viewModels()
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