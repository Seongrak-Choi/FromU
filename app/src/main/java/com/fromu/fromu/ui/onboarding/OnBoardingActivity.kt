package com.fromu.fromu.ui.onboarding

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.fromu.fromu.FromUApplication
import com.fromu.fromu.databinding.ActivityOnBoardingBinding
import com.fromu.fromu.ui.LoginActivity
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.utils.PrefManager

class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding>(ActivityOnBoardingBinding::inflate) {

//    val notificationPermissionLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//            if (isGranted) {
//                //권한 허용
//
//            } else {
//                //권한 거부
//                //TODO 추후 한 번 더 확인용 다이얼로그 디자인 받기
//            }
//        }

//    val storagePermissionLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//            if (isGranted) {
//                //권한 허용
//
//            } else {
//                //권한 거부
//                //TODO 추후 한 번 더 확인용 다이얼로그 디자인 받기
//            }
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initView()
    }

    private fun initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            //푸시 알림 권한 체크 및 허용 알림
            checkNotificationPermission()
        }
    }

    private fun initView() {

        initViewPager()
        initEvent()
    }

    private fun initViewPager() {
        binding.apply {
            vpOnBoarding.apply {
                adapter = OnBoardingVpAdapter(this@OnBoardingActivity)
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        when (position) {
                            0, 1 -> binding.isLastPage = false
                            2 -> binding.isLastPage = true
                        }
                    }
                })
            }
            indicatorOnBoarding.setViewPager2(vpOnBoarding)
        }
    }

    private fun initEvent() {
        binding.apply {
            tvOnBoardingNext.setOnClickListener {
                vpOnBoarding.currentItem = vpOnBoarding.currentItem + 1
            }

            tvOnBoardingStarting.setOnClickListener {
                Intent(this@OnBoardingActivity, LoginActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()

        // 온보딩 봤다고 체크
        FromUApplication.prefManager.editor.putBoolean(PrefManager.INIT_ON_BOARDING_KEY, true).apply()
    }
}