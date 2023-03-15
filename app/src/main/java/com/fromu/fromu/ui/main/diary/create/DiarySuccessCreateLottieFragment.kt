package com.fromu.fromu.ui.main.diary.create

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.databinding.FragmentDiarySuccessCreateLottieBinding
import com.fromu.fromu.ui.base.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DiarySuccessCreateLottieFragment : BaseFragment<FragmentDiarySuccessCreateLottieBinding>(FragmentDiarySuccessCreateLottieBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initData() {
        lifecycleScope.launch {
            delay(1500)
            findNavController().popBackStack()
        }
    }

    private fun initView() {
        binding.apply {}

        initEvent()
    }

    private fun initEvent() {
        binding.apply {}
    }
}