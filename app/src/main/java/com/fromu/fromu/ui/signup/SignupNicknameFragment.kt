package com.fromu.fromu.ui.signup

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.fromu.fromu.R
import com.fromu.fromu.databinding.FragmentSignupNicknameBinding
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Extension.debounce
import com.fromu.fromu.viewmodels.SignupViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class SignupNicknameFragment : BaseFragment<FragmentSignupNicknameBinding>(FragmentSignupNicknameBinding::inflate) {

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
            lifecycleOwner = this@SignupNicknameFragment
            vm = signupViewModel
        }
        setEvent()
    }


    private fun setEvent() {
        binding.apply {
            etContents.debounce(coroutineScope = CoroutineScope(Dispatchers.Main)) {
                if (it.isEmpty()) {
                    signupViewModel.isNicknameMatch.value = false
                } else {
                    signupViewModel.isNicknameMatch.value = checkPattern(it).apply {
                        if (this) {
                            setNicknameNormalUi()
                            signupViewModel.nickname.value = it
                        } else
                            setNicknameErrorUi()

                    }
                }
            }

            etContents.doAfterTextChanged {
                setNicknameNormalUi()
            }
        }
    }


    /**
     * 정규화에 일치하지 않은 경우의 입력 UI 셋팅
     *
     */
    private fun setNicknameErrorUi() {
        binding.apply {
            vNicknameUnderline.background = ContextCompat.getDrawable(requireContext(), R.color.color_ff4a6b)
            tvWarringMsg.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_ff4a6b))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                etContents.textCursorDrawable = ContextCompat.getDrawable(requireContext(), R.color.color_ff4a6b)
        }
    }

    /**
     *  일반적인 경우의 입력 UI 셋팅
     *
     */
    private fun setNicknameNormalUi() {
        binding.apply {
            vNicknameUnderline.background = ContextCompat.getDrawable(requireContext(), R.color.color_a735ff)
            tvWarringMsg.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_6f6f6f))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                etContents.textCursorDrawable = ContextCompat.getDrawable(requireContext(), R.color.color_a735ff)
        }
    }


    private fun checkPattern(str: String): Boolean {
        return str.matches(Regex(Const.ONLY_KOREAN_EXPRESSION))
    }
}