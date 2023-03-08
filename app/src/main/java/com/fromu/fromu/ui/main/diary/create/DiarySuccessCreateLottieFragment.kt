package com.fromu.fromu.ui.main.diary.create

import android.animation.Animator
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.databinding.FragmentDiarySuccessCreateLottieBinding
import com.fromu.fromu.ui.base.BaseFragment

class DiarySuccessCreateLottieFragment : BaseFragment<FragmentDiarySuccessCreateLottieBinding>(FragmentDiarySuccessCreateLottieBinding::inflate) {
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
            lottieDiarySuccessCreate.playAnimation()
        }

        initEvent()
    }

    private fun initEvent() {
        binding.apply {
            lottieDiarySuccessCreate.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                    //Nothing
                }

                override fun onAnimationEnd(p0: Animator) {
                    findNavController().popBackStack()
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
}