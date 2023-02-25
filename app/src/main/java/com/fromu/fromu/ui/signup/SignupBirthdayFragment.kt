package com.fromu.fromu.ui.signup

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.fragment.app.viewModels
import com.fromu.fromu.databinding.FragmentSignupBirthdayBinding
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.utils.DateInputFilter
import com.fromu.fromu.viewmodels.SignupViewModel


class SignupBirthdayFragment: BaseFragment<FragmentSignupBirthdayBinding>(FragmentSignupBirthdayBinding::inflate) {

    private val signupViewModel : SignupViewModel by viewModels()

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
            lifecycleOwner = this@SignupBirthdayFragment
            vm = signupViewModel

            etBirthdayContents.filters = arrayOf<InputFilter>(DateInputFilter())
        }

        initEvent()
    }

    private fun initEvent() {
        binding.apply {}
    }
}