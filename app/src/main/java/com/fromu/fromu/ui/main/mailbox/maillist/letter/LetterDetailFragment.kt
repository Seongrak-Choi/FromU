package com.fromu.fromu.ui.main.mailbox.maillist.letter

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.response.ReadLetterRes
import com.fromu.fromu.databinding.FragmentLetterDetailBinding
import com.fromu.fromu.databinding.PopupLetterDetailMenuBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.LetterDetailViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LetterDetailFragment : BaseFragment<FragmentLetterDetailBinding>(FragmentLetterDetailBinding::inflate) {

    private val letterDetailViewModel: LetterDetailViewModel by viewModels()
    private var letterId: Int = 0 // 조회할 편지의 id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callApi()
        initView()
        initObserve()
        initEvent()
    }

    private fun initData() {
        getArgs()
    }

    private fun initView() {
        binding.apply {
            lifecycleOwner = this@LetterDetailFragment
            vm = letterDetailViewModel
        }
    }

    private fun callApi() {
        letterDetailViewModel.readLetter(letterId)
    }

    private fun initObserve() {
        letterDetailViewModel.apply {
            readLetterResult.observe(viewLifecycleOwner) { resources ->
                handleResource(resources, listener = object : ResourceSuccessListener<ReadLetterRes> {
                    override fun onSuccess(res: ReadLetterRes) {
                        handleReadLetterRes(res)
                    }
                })
            }
        }
    }

    private fun initEvent() {
        binding.apply {
            layoutFirstOpenLottie.lottieFirstOpen.addAnimatorListener(object : AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                }

                override fun onAnimationEnd(p0: Animator) {
                    letterDetailViewModel.readFlag.value = true
                }

                override fun onAnimationCancel(p0: Animator) {
                }

                override fun onAnimationRepeat(p0: Animator) {
                }
            })

            // 메뉴 버튼
            ivLetterDetailMenu.setOnClickListener {
                showPopupMenu(it, layoutInflater)
            }

            // 답장하기 or 감사인사하러 가기 버튼
            tvLetterDetailReply.setOnClickListener {
                if (letterDetailViewModel.letterDetail.value?.status == 0) {
                    val action = LetterDetailFragmentDirections.actionLetterDetailFragmentToReplyLetterFragment(letterId)
                    findNavController().navigate(action)
                } else if (letterDetailViewModel.letterDetail.value?.status == 2) {
                    val action = LetterDetailFragmentDirections.actionLetterDetailFragmentToRateLetterFragment(letterId)
                    findNavController().navigate(action)
                }
            }

            // 뒤로가기 버튼
            ivLetterDetailBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun getArgs() {
        val args by navArgs<LetterDetailFragmentArgs>()
        letterId = args.letterId
        letterDetailViewModel.readFlag.value = args.readFlag
    }

    /**
     * 편지 조회 결과
     *
     * @param res
     */
    private fun handleReadLetterRes(res: ReadLetterRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                letterDetailViewModel.letterDetail.value = res.result
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }

    /**
     * show 메뉴 팝업
     */
    private fun showPopupMenu(
        targetView: View,
        layoutInflater: LayoutInflater
    ) {
        val popupView = PopupLetterDetailMenuBinding.inflate(layoutInflater)
        val mPopupWindow = PopupWindow(
            popupView.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        popupView.tvLetterDetailReport.setOnClickListener {
            val action = LetterDetailFragmentDirections.actionLetterDetailFragmentToReportLetterFragment(letterId)
            findNavController().navigate(action)
            mPopupWindow.dismiss()
        }

        // popupWindow를 제외한 다른 부분 선택시 메뉴가 꺼지도록 popupWindow에 포커스를 줌
        mPopupWindow.isFocusable = true
        mPopupWindow.animationStyle = R.style.PopupWindowUpDownAnimation

        // 앱바 밑에 출력 되도록 셋팅
        mPopupWindow.showAsDropDown(targetView)
    }
}