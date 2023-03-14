package com.fromu.fromu.ui.main.notification

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.databinding.FragmentNotificationBinding
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.main.MainActivity

class NotificationFragment : BaseFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initEvent()
    }

    private fun initData() {}
    private fun initView() {
        activity?.let { activity ->
            if (activity is MainActivity) {
                activity.isVisibleBottomNav(false)
                activity.isVisibleAppbar(false)
            }
        }
    }

    private fun initEvent() {
        binding.apply {
            ivNotificationBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}