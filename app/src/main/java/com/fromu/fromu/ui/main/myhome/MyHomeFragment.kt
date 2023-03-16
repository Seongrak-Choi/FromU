package com.fromu.fromu.ui.main.myhome

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.response.CheckMatchingRes
import com.fromu.fromu.databinding.FragmentMyHomeBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.main.MainActivity
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.MyHomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyHomeFragment : BaseFragment<FragmentMyHomeBinding>(FragmentMyHomeBinding::inflate) {

    private val myHomeViewModel: MyHomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObserve()
        callApi()
        initEvent()
    }

    private fun initData() {}

    private fun callApi() {
        myHomeViewModel.getCoupleInfo()
    }

    private fun initView() {
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).apply {
                isVisibleBottomNav(true)
                isVisibleAppbar(true)
            }
        }

        binding.apply {
            lifecycleOwner = this@MyHomeFragment
            vm = myHomeViewModel
        }
    }

    private fun initObserve() {
        myHomeViewModel.coupleInfoResult.observe(viewLifecycleOwner) { resources ->
            handleResource(resources, listener = object : ResourceSuccessListener<CheckMatchingRes> {
                override fun onSuccess(res: CheckMatchingRes) {
                    handleCoupleInfoResult(res)
                }
            })
        }
    }


    private fun initEvent() {
        binding.apply {

            // 알람 메세지 설정
            clAlarmMsgSetting.setOnClickListener {
                findNavController().navigate(R.id.action_myHomeFragment_to_alarmMsgSettingFragment)
            }

            // 우편함 설정
            clMailBoxSetting.setOnClickListener {

            }
        }
    }


    /**
     * 커플 정보 결과 핸들링
     *
     * @param res
     */
    private fun handleCoupleInfoResult(res: CheckMatchingRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                myHomeViewModel.couple.value = res.result.coupleRes
                if (res.result.dday != 0) {
                    myHomeViewModel.dday.value = res.result.dday.toString()
                }
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }


}