package com.fromu.fromu.ui.calendar

import android.os.Bundle
import android.view.View
import com.fromu.fromu.databinding.FragmentCalendarBinding
import com.fromu.fromu.ui.base.BaseFragment

class CalendarFragment: BaseFragment<FragmentCalendarBinding>(FragmentCalendarBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initData() {}
    private fun initView() {}
}