package com.fromu.fromu.ui.main.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.data.remote.network.response.GetNoticeRes
import com.fromu.fromu.databinding.FragmentNotificationBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.main.MainActivity
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.MainViewModel

class NotificationFragment : BaseFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var notificationRvAdapter: NotificationRvAdapter

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
        callApi()
    }

    private fun initView() {
        activity?.let { activity ->
            if (activity is MainActivity) {
                activity.isVisibleBottomNav(false)
                activity.isVisibleAppbar(false)
            }
        }

        binding.apply {
            lifecycleOwner = this@NotificationFragment
            vm = mainViewModel
        }

        settingNotificationRv()
    }

    private fun initEvent() {
        binding.apply {
            ivNotificationBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initObserve() {
        mainViewModel.apply {
            getNoticeResult.observe(viewLifecycleOwner) { resources ->
                handleResource(resources, true, object : ResourceSuccessListener<GetNoticeRes> {
                    override fun onSuccess(res: GetNoticeRes) {
                        when (res.code) {
                            Const.SUCCESS_CODE -> {
                                notificationRvAdapter.submitList(res.result)
                            }
                            else -> {
                                Utils.showNetworkErrorSnackBar(binding.root)
                            }
                        }
                    }
                })
            }
        }
    }

    private fun callApi() {
        mainViewModel.getNotice()
    }

    private fun settingNotificationRv() {
        notificationRvAdapter = NotificationRvAdapter()
        binding.rvNotification.adapter = notificationRvAdapter
    }

}