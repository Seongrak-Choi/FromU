package com.fromu.fromu.ui.signup

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.R
import com.fromu.fromu.databinding.FragmentSignupBirthdayBinding
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.utils.InputFilterMinMax
import com.fromu.fromu.viewmodels.SignupViewModel


class SignupBirthdayFragment : BaseFragment<FragmentSignupBirthdayBinding>(FragmentSignupBirthdayBinding::inflate) {

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
            lifecycleOwner = this@SignupBirthdayFragment
            vm = signupViewModel
        }

        initEvent()
    }

    @SuppressLint("SetTextI18n")
    private fun initEvent() {
        binding.apply {

            // 년
            etBirthdayYear.apply {
                doAfterTextChanged {
                    signupViewModel.isValidBirthday.value = checkValidation()

                    if (it.toString().length >= 4) {
                        etBirthdayMonth.requestFocus()
                    }
                }

                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE -> {
                            etBirthdayMonth.requestFocus()
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }

                onFocusChangeListener = View.OnFocusChangeListener { v, hasFocuse ->
                    if (hasFocuse) {
                        this.text?.clear()
                        vBirthdayUnderline.isSelected = true
                    }
                }
            }

            // 월
            etBirthdayMonth.apply {
                filters = arrayOf<InputFilter>(InputFilterMinMax("1", "12"))

                doAfterTextChanged {
                    signupViewModel.isValidBirthday.value = checkValidation()

                    if (it.toString().length >= 2) {
                        etBirthdayDay.requestFocus()
                    }
                }

                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE -> {
                            etBirthdayDay.requestFocus()
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }

                onFocusChangeListener = View.OnFocusChangeListener { v, hasFocuse ->
                    if (hasFocuse) {
                        this.text?.clear()
                        vBirthdayUnderline.isSelected = true
                    } else {
                        if (this.text.toString().length == 1) {
                            signupViewModel.isValidBirthday.value = checkValidation()
                            this.setText("0${this.text.toString()}")
                        }
                    }
                }
            }


            // 일
            etBirthdayDay.apply {
                filters = arrayOf<InputFilter>(InputFilterMinMax("1", "31"))

                doAfterTextChanged {
                    signupViewModel.isValidBirthday.value = checkValidation()
                }

                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE -> {
                            etBirthdayDay.clearFocus()
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }

                onFocusChangeListener = View.OnFocusChangeListener { v, hasFocuse ->
                    if (hasFocuse) {
                        this.text?.clear()
                        vBirthdayUnderline.isSelected = true
                    } else {
                        this.setText("0${this.text.toString()}")
                        signupViewModel.isValidBirthday.value = checkValidation()
                    }
                }
            }

            // 다음 버튼 클릭
            clBirthdayNext.setOnClickListener {
                signupViewModel.birthday.value = "${etBirthdayYear.text}${etBirthdayMonth.text}${etBirthdayDay.text}"
                findNavController().navigate(R.id.action_signupBirthdayFragment_to_signupGenderFragment)
            }

            // back 버튼
            ivBirthdayBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    /**
     * 생년월일 다 입력 했는지 확인하기 위함
     */
    private fun checkValidation(): Boolean {
        binding.apply {
            return etBirthdayYear.text.toString().length == 4 && etBirthdayMonth.text.toString().length == 2 && etBirthdayDay.text.toString().length == 2
        }
    }


}