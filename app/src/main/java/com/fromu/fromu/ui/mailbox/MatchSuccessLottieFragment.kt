package com.fromu.fromu.ui.mailbox

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.fromu.fromu.databinding.FragmentMatchSuccessLottieBinding
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.viewmodels.InvitationViewModel

class MatchSuccessLottieFragment : BaseFragment<FragmentMatchSuccessLottieBinding>(FragmentMatchSuccessLottieBinding::inflate) {

    private val invitationViewModel: InvitationViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initData() {}
    private fun initView() {
        binding.apply {
            lifecycleOwner = this@MatchSuccessLottieFragment
            vm = invitationViewModel
        }
    }
}