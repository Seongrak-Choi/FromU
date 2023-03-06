package com.fromu.fromu.ui.main.diary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.R
import com.fromu.fromu.databinding.FragmentDiaryBinding
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.main.MainActivity
import com.fromu.fromu.viewmodels.DiaryViewModel

class DiaryFragment : BaseFragment<FragmentDiaryBinding>(FragmentDiaryBinding::inflate) {

    private val diaryViewModel: DiaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initData() {
    }

    private fun initView() {
        (requireActivity() as MainActivity).apply {
            setVisibleAppbar(true)
            setVisibleBottomNav(true)
        }

        binding.apply {
            lifecycleOwner = this@DiaryFragment
            vm = diaryViewModel
        }

        initEvent()
    }

    private fun initEvent() {
        binding.apply {

            // 디데이
            vDiaryDday.setOnClickListener {
                //TODO 추후 디데이 설정 되었는지 체크해서 안 되어 있을 때만, 설정할 수 있는 곳으로 넘어갈 수 있도록 핸들링
                if (true) {
                    (requireActivity() as MainActivity).apply {
                        setVisibleBottomNav(false)
                        setVisibleAppbar(false)
                    }
                    findNavController().navigate(R.id.action_diaryFragment_to_firstMetDayFragment)
                }
            }

            //일기장 추가
            ivDiaryAdd.setOnClickListener {
                //TODO 일기장 추가 화면 이동
            }

        }
    }
}