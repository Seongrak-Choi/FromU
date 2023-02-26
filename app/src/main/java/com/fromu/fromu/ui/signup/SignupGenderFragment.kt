package com.fromu.fromu.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.fromu.fromu.databinding.FragmentSignupGenderBinding
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.viewmodels.SignupViewModel

class SignupGenderFragment : BaseFragment<FragmentSignupGenderBinding>(FragmentSignupGenderBinding::inflate) {
    private val signupViewModel: SignupViewModel by viewModels()

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
            lifecycleOwner = this@SignupGenderFragment
            vm = signupViewModel
        }

        initEvent()
    }

    private fun initEvent() {
        binding.apply {

        }
    }
}