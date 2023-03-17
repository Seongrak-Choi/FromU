package com.fromu.fromu.ui.main.mailbox.stampbox

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fromu.fromu.data.remote.network.response.FromCountRes
import com.fromu.fromu.databinding.ActivityStampBoxBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Logger
import com.fromu.fromu.utils.UiUtils
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.StampBoxViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StampBoxActivity : BaseActivity<ActivityStampBoxBinding>(ActivityStampBoxBinding::inflate) {
    private val stampBoxViewModel: StampBoxViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initView()
        initObserve()
        initEvent()
    }


    private fun initData() {
        // 보유 프롬 조회 api 호출
        stampBoxViewModel.getFromCount()
    }

    private fun initView() {
        UiUtils.setFullScreenWithStatusBar(this)
        settingNavController()

        binding.apply {
            vm = stampBoxViewModel
        }
    }

    private fun initObserve() {
        stampBoxViewModel.getFromCountResult.observe(this) { resources ->
            handleResource(resources, false, object : ResourceSuccessListener<FromCountRes> {
                override fun onSuccess(res: FromCountRes) {
                    handleFromCountRes(res)
                }
            })
        }
    }

    private fun initEvent() {
        binding.apply {}
    }

    /**
     * Navigation Component 셋팅
     */
    private fun settingNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fcvStampBox.id) as NavHostFragment
        navController = navHostFragment.navController
    }

    /**
     * 프롬수 조회 결과 핸들링
     *
     * @param res
     */
    private fun handleFromCountRes(res: FromCountRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                stampBoxViewModel.fromCountFlow.value = res.result
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }
}