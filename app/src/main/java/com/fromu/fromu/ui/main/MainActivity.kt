package com.fromu.fromu.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.fromu.fromu.databinding.ActivityMainBinding
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

    /**
     * bottomNav와 navigation component 셋팅
     */
    private fun settingBottomNavWithNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fcvMain.id) as NavHostFragment
        navController = navHostFragment.navController

        binding.bnvMain.setupWithNavController(navController)
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