package com.fromu.fromu.ui.main.mailbox.maillist.letter.reply

import android.animation.Animator
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.response.PostLetterRes
import com.fromu.fromu.databinding.FragmentWriteLetterBinding
import com.fromu.fromu.model.WriteType
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.dialog.DialogPopupTwoBtn
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.KeyboardVisibilityUtils
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.WriteLetterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReplyLetterFragment : BaseFragment<FragmentWriteLetterBinding>(FragmentWriteLetterBinding::inflate) {
    private val writeLetterViewModel: WriteLetterViewModel by viewModels()

    // 키보드 show에 따른 스크롤 위치 변경 유틸 클래스
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObserve()
        initEvent()
    }

    private fun initData() {
        setArgs()
    }

    private fun initView() {
        settingKeyBoardVisibilityUtils()
        binding.apply {
            lifecycleOwner = this@ReplyLetterFragment
            vm = writeLetterViewModel
            writeType = WriteType.REPLY
        }
    }

    private fun initEvent() {
        binding.apply {

            // 편지지
            clWriteLetter.setOnClickListener {
                etWriteLetter.requestFocus()
                openKeyboard(etWriteLetter)
            }

            // 편지 보내기 버튼
            tvWriteLetterSend.setOnClickListener {
                DialogPopupTwoBtn(getString(R.string.check_reply), getString(R.string.back), getString(R.string.reply_letter), object : DialogPopupTwoBtn.DialogPopupTwoBtnListener {
                    override fun onNegative() {
                        //Nothing
                    }

                    override fun onPositive() {
                        sendLetter()
                    }
                }).show(childFragmentManager, DialogPopupTwoBtn.TAG)
            }

            // 편지 전송 로티
            layoutPostLetterSuccessLottie.lottiePostLetterSuccess.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {}
                override fun onAnimationEnd(p0: Animator) {
                    lottieFinish()
                }

                override fun onAnimationCancel(p0: Animator) {}
                override fun onAnimationRepeat(p0: Animator) {}
            })
        }
    }

    private fun initObserve() {
        writeLetterViewModel.postLetterResult.observe(viewLifecycleOwner) { resources ->
            handleResource(resources, true, object : ResourceSuccessListener<PostLetterRes> {
                override fun onSuccess(res: PostLetterRes) {
                    handlePostLetterRes(res)
                }
            })
        }
    }

    private fun setArgs() {
        val args: ReplyLetterFragmentArgs by navArgs()
        writeLetterViewModel.letterId.value = args.letterId
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

    private fun handlePostLetterRes(res: PostLetterRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                writeLetterViewModel.receiveMailBoxName.value = res.result.receiveMailboxName
                writeLetterViewModel.isPostSuccess.value = true
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }


    fun lottieFinish() {
        findNavController().popBackStack()
    }

    private fun sendLetter() {
        writeLetterViewModel.postReplyLetter()
    }

    override fun onDestroyView() {
        keyboardVisibilityUtils.detachKeyboardListeners()

        super.onDestroyView()
    }
}