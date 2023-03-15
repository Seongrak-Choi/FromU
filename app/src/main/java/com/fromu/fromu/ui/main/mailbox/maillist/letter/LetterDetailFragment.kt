package com.fromu.fromu.ui.main.mailbox.maillist.letter

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.fromu.fromu.data.remote.network.response.ReadLetterRes
import com.fromu.fromu.databinding.FragmentLetterDetailBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.LetterDetailViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LetterDetailFragment : BaseFragment<FragmentLetterDetailBinding>(FragmentLetterDetailBinding::inflate) {

    private val letterDetailViewModel: LetterDetailViewModel by viewModels()

    private var letterId: Int = 25 // 조회할 편지의 id

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
        getArgs()
        callApi()
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
        }
    }

    private fun getArgs() {
        val args by navArgs<LetterDetailFragmentArgs>()
        letterId = args.letterId
//        letterDetailViewModel.readFlag.value = args.readFlag
        letterDetailViewModel.readFlag.value = false
    }

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
}