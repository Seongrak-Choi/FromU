package com.fromu.fromu.ui.invitaion

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.fromu.fromu.databinding.FragmentMatchSuccessLottieBinding
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.mailbox.DecideMailBoxNameActivity
import com.fromu.fromu.viewmodels.InvitationViewModel

class MatchSuccessLottieFragment : BaseFragment<FragmentMatchSuccessLottieBinding>(FragmentMatchSuccessLottieBinding::inflate) {

    private val invitationViewModel: InvitationViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initData() {
        blockBackPressed()
    }

    private fun initView() {
        binding.apply {
            lifecycleOwner = this@MatchSuccessLottieFragment
            vm = invitationViewModel

            lottieMatchSuccess.playAnimation()
        }

        initEvent()
    }

    private fun initEvent() {
        binding.apply {
            lottieMatchSuccess.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                    //Nothing
                }

                override fun onAnimationEnd(p0: Animator) {
                    Intent(requireContext(), DecideMailBoxNameActivity::class.java).apply {
                        startActivity(this)
                    }
                    requireActivity().finish()
                }

                override fun onAnimationCancel(p0: Animator) {
                    //Nothing
                }

                override fun onAnimationRepeat(p0: Animator) {
                    //Nothing
                }
            })
        }
    }


    private fun blockBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //Nothing
            }
        })
    }
}