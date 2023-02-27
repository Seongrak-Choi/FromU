package com.fromu.fromu.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.fromu.fromu.databinding.FragmentTermsBinding
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.viewmodels.SignupViewModel

class TermsFragment : BaseFragment<FragmentTermsBinding>(FragmentTermsBinding::inflate) {

    private val signupViewModel: SignupViewModel by activityViewModels()

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
            lifecycleOwner = this@TermsFragment
            vm = signupViewModel

        }

        initEvent()
    }

    private fun initEvent() {
        binding.apply {

        }
    }
}