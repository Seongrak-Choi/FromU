package com.fromu.fromu.ui.main.mailbox.write

import android.os.Bundle
import android.view.View
import com.fromu.fromu.databinding.FragmentWriteLetterBinding
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.utils.KeyboardVisibilityUtils
import com.fromu.fromu.utils.Logger

class WriteLetterFragment : BaseFragment<FragmentWriteLetterBinding>(FragmentWriteLetterBinding::inflate) {

    // 키보드 show에 따른 스크롤 위치 변경 유틸 클래스
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initEvent()
    }

    private fun initData() {}
    private fun initView() {
        settingKeyBoardVisibilityUtils()
        binding.apply {
            stampId = 1
        }
    }

    private fun initEvent() {
        binding.apply {
            clWriteLetter.setOnClickListener {
                Logger.e("rak", "여기 안 눌림??")
                etWriteLetter.requestFocus()
            }
        }
    }

    /**
     * 키보드 높이 만큼 스크롤을 내려서 입력창을 보이도록 셋팅
     */
    private fun settingKeyBoardVisibilityUtils() {
        keyboardVisibilityUtils = KeyboardVisibilityUtils(requireActivity().window, onShowKeyboard = { keyboardHeight ->
            binding.nsvWriteLetter.run {
                smoothScrollTo(scrollX, scrollY + keyboardHeight)
            }
        })
    }


    override fun onDestroyView() {
        keyboardVisibilityUtils.detachKeyboardListeners()

        super.onDestroyView()
    }
}