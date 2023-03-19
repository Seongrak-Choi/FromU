package com.fromu.fromu.ui.main.calendar

import android.os.Bundle
import android.view.View
import com.fromu.fromu.R
import com.fromu.fromu.databinding.FragmentCalendarBinding
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class CalendarFragment : BaseFragment<FragmentCalendarBinding>(FragmentCalendarBinding::inflate) {
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
        activity?.let { activity ->
            if (activity is MainActivity) {
                activity.isVisibleBottomNav(true)
                activity.isVisibleAppbar(true)
            }
        }

        binding.apply {
            lifecycleOwner = this@CalendarFragment
            calendar.setHeaderTextAppearance(R.style.CalendarWidgetHeader)
        }
    }
}