package com.fromu.fromu.ui.signup

import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.R
import com.fromu.fromu.databinding.FragmentSignupNicknameBinding
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Extension.debounce
import com.fromu.fromu.viewmodels.SignupViewModel


class SignupNicknameFragment : BaseFragment<FragmentSignupNicknameBinding>(FragmentSignupNicknameBinding::inflate) {

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
            lifecycleOwner = this@SignupNicknameFragment
            vm = signupViewModel
        }

        initEvent()

    }


    private fun initEvent() {
        binding.apply {

            etContents.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
                vNicknameUnderline.isSelected = hasFocus
            }

            etContents.debounce(coroutineScope = lifecycleScope) {
                if (it.isEmpty()) {
                    signupViewModel.isValidNickname.value = false
                } else {
                    if (checkPattern(it)) {
                        signupViewModel.isValidNickname.value = checkPattern(it)
                        signupViewModel.nickname.value = it
                        setPassNicknameUi()
                    } else {
                        setNicknameErrorUi()
                    }
                }
            }

            // 닉네임 입력창
            etContents.doAfterTextChanged {
                signupViewModel.isValidNickname.value = false
                setNicknameNormalUi()
            }

            // 다음 버튼
            tvNicknameNext.setOnClickListener {
                findNavController().navigate(R.id.action_signupNicknameFragment_to_signupBirthdayFragment)
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
            tvWarringMsg.visibility = View.VISIBLE
            tvWarringMsg.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_ff4a6b))
        }
    }


    /**
     *  일반적인 경우의 입력 UI 셋팅
     *
     */
    private fun setNicknameNormalUi() {
        binding.apply {
            vNicknameUnderline.background = ContextCompat.getDrawable(requireContext(), R.color.color_a735ff)
            tvWarringMsg.visibility = View.VISIBLE
            tvWarringMsg.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_6f6f6f))
        }
    }

    /**
     * 정규식에 통과했을 때 입력 UI 셋팅
     *
     */
    private fun setPassNicknameUi() {
        binding.apply {
            vNicknameUnderline.background = ContextCompat.getDrawable(requireContext(), R.color.color_dedee2)
            tvWarringMsg.visibility = View.GONE
        }
    }


    /**
     * 입력한 닉네임 정규화 확인 메소드
     *
     * @param str
     * @return
     */
    private fun checkPattern(str: String): Boolean {
        return str.matches(Regex(Const.NO_SPECIAL_CHAR_AND_NO_GAP_EXPRESSION))
    }
}