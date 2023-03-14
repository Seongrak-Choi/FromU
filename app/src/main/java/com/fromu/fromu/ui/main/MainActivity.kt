package com.fromu.fromu.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.fromu.fromu.R
import com.fromu.fromu.databinding.ActivityMainBinding
import com.fromu.fromu.model.BottomMenuType
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.utils.UiUtils
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private lateinit var navController: NavController
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initView()
        initEvent()
    }

    private fun initData() {}

    private fun initView() {
        UiUtils.setFullScreenWithStatusBar(this)

        settingBottomNavWithNavController()

        binding.apply {
            lifecycleOwner = this@MainActivity
            vm = mainViewModel
        }

        initBackPress()
    }

    private fun initEvent() {
        binding.apply {
            bnvMain.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_diary -> {
                        mainViewModel.type.value = BottomMenuType.DIARY.type
                        NavigationUI.onNavDestinationSelected(it, navController)
                    }
                    R.id.nav_calendar -> {
                        mainViewModel.type.value = BottomMenuType.CALENDAR.type
                        NavigationUI.onNavDestinationSelected(it, navController)
                    }
                    R.id.nav_mail_box -> {
                        mainViewModel.type.value = BottomMenuType.MAIL_BOX.type
                        NavigationUI.onNavDestinationSelected(it, navController)
                    }
                    R.id.nav_my_home -> {
                        mainViewModel.type.value = BottomMenuType.MY_HOME.type
                        NavigationUI.onNavDestinationSelected(it, navController)
                    }
                    else -> {
                        false
                    }
                }
            }

            ivAppbarSetting.setOnClickListener {
                isVisibleAppbar(false)
                navController.navigate(R.id.action_myHomeFragment_to_settingFragment)
            }

            ivAppbarAlarm.setOnClickListener {
                navController.navigate(R.id.action_global_notificationFragment)
            }
        }
    }

    /**
     * bottomNav와 navigation component 셋팅
     */
    private fun settingBottomNavWithNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fcvMain.id) as NavHostFragment
        navController = navHostFragment.navController

        binding.bnvMain.setupWithNavController(navController)
    }


    fun isVisibleAppbar(visible: Boolean) {
        binding.mainAppbar.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun isVisibleBottomNav(visible: Boolean) {
        binding.bnvMain.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun initBackPress() {
        backPressed(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Utils.exitApp(this@MainActivity)
            }
        })
    }
}