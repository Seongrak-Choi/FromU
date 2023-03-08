package com.fromu.fromu.ui.main.diary.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.R
import com.fromu.fromu.data.dto.DiaryCoverDto
import com.fromu.fromu.databinding.FragmentDiaryPickCoverBinding
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.main.MainActivity
import com.fromu.fromu.ui.main.diary.PickCoverRvAdapter
import com.fromu.fromu.viewmodels.CreateDiaryViewModel

class DiaryPickCoverFragment : BaseFragment<FragmentDiaryPickCoverBinding>(FragmentDiaryPickCoverBinding::inflate) {

    private val createDiaryViewModel: CreateDiaryViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initData() {
        activity?.let {
            if (it is MainActivity)
                it.isVisibleBottomNav(false)
        }
    }

    private fun initView() {
        binding.apply {
            lifecycleOwner = this@DiaryPickCoverFragment
            vm = createDiaryViewModel
        }

        initEvent()
        initAdapter()
    }

    private fun initEvent() {
        binding.apply {

            // 다음 버튼
            tvPickCoverNext.setOnClickListener {
                findNavController().navigate(R.id.action_createDiaryPickCoverFragment_to_diaryDecideNameFragment)
            }

            // 백 버튼
            ivPickCoverBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initAdapter() {
        binding.rvPickCover.adapter = PickCoverRvAdapter(object : PickCoverRvAdapter.PickCoverRvListener {
            override fun onClick(diaryCoverDto: DiaryCoverDto) {
                createDiaryViewModel.selectCoverNum.value = diaryCoverDto.coverId
            }
        })
    }
}