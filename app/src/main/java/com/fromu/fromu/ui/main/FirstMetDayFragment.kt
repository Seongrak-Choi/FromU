package com.fromu.fromu.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.databinding.FragmentFirstMetDayBinding
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.utils.InputFilterMinMax
import com.fromu.fromu.utils.Logger
import com.fromu.fromu.viewmodels.FirstMetDayViewModel

class FirstMetDayFragment : BaseFragment<FragmentFirstMetDayBinding>(FragmentFirstMetDayBinding::inflate) {

    private val firstMetDayViewModel: FirstMetDayViewModel by viewModels()

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

        }
        initEvent()
    }

    @SuppressLint("SetTextI18n")
    private fun initEvent() {
        binding.apply {
            // 년
            etFirstMetDayYear.apply {
                doAfterTextChanged {
                    firstMetDayViewModel.isValidFirstMetDay.value = checkValidation()

                    if (it.toString().length >= 4) {
                        etFirstMetDayMonth.requestFocus()
                    }
                }

                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE -> {
                            etFirstMetDayMonth.requestFocus()
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }

                onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        this.text?.clear()
                        vFirstMetDayUnderline.isSelected = true
                    }
                }
            }

            // 월
            etFirstMetDayMonth.apply {
                filters = arrayOf<InputFilter>(InputFilterMinMax("0", "12"))

                doAfterTextChanged {
                    if (this.text.toString().isNotEmpty()) {
                        if (this.text.toString().toInt() == 0 && this.text.toString().length == 2) {
                            setText("0")
                        } else {
                            firstMetDayViewModel.isValidFirstMetDay.value = checkValidation()

                            if (it.toString().length >= 2) {
                                etFirstMetDayDay.requestFocus()
                            }
                        }
                    }
                }

                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE -> {
                            etFirstMetDayDay.requestFocus()
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }

                onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        this.text?.clear()
                        vFirstMetDayUnderline.isSelected = true
                    } else {
                        if (this.text.toString().length == 1) {
                            this.setText("0${this.text.toString()}")
                        }
                    }
                    firstMetDayViewModel.isValidFirstMetDay.value = checkValidation()
                }
            }


            // 일
            etFirstMetDayDay.apply {
                filters = arrayOf<InputFilter>(InputFilterMinMax("0", "31"))

                doAfterTextChanged {
                    if (this.text.toString().isNotEmpty()) {
                        if (this.text.toString().toInt() == 0 && this.text.toString().length == 2) {
                            setText("0")
                        } else {
                            firstMetDayViewModel.isValidFirstMetDay.value = checkValidation()
                        }
                    }
                }

                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE -> {
                            etFirstMetDayDay.clearFocus()
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }

                onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        this.text?.clear()
                        vFirstMetDayUnderline.isSelected = true
                    } else {
                        if (this.text.toString().length == 1) {
                            this.setText("0${this.text.toString()}")
                        }
                    }
                    firstMetDayViewModel.isValidFirstMetDay.value = checkValidation()
                }
            }

            // 다음 버튼 클릭
            tvFirstMetDayNext.setOnClickListener {
                firstMetDayViewModel.firstMetDay.value = "${etFirstMetDayYear.text}${etFirstMetDayMonth.text}${etFirstMetDayDay.text}"
                Logger.e("rak", firstMetDayViewModel.firstMetDay.value.toString())
            }

            // back 버튼
            ivFirstMetDayBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    /**
     * 생년월일 다 입력 했는지 확인하기 위함
     */
    private fun checkValidation(): Boolean {
        binding.apply {
            return etFirstMetDayYear.text.toString().length == 4 && etFirstMetDayMonth.text.toString().length == 2 && etFirstMetDayDay.text.toString().length == 2
        }
    }
}