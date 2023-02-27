package com.fromu.fromu.ui.invitaion

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.fromu.fromu.databinding.FragmentInvitationMainBinding
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.InvitationViewModel

class InvitationMainFragment : BaseFragment<FragmentInvitationMainBinding>(FragmentInvitationMainBinding::inflate) {
    private val invitationViewModel: InvitationViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initData() {

    }

    private fun initView() {
        binding.apply {
            vm = invitationViewModel
            lifecycleOwner = this@InvitationMainFragment
        }

        initEvent()
    }

    private fun initEvent() {
        binding.apply {
            ivDescriptionClose.setOnClickListener {
                clDescription.visibility = View.INVISIBLE
            }

            //TODO 토스트 테스트
            ivInvitationRefresh.setOnClickListener {
                Utils.showCustomSnackBar(binding.root, "연인과의 연결이 실행되지 않았어!")
            }
        }
    }
}