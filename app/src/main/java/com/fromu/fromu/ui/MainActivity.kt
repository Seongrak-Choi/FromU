package com.fromu.fromu.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.fromu.fromu.R
import com.fromu.fromu.databinding.ActivityMainBinding
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.utils.UiUtils
import com.fromu.fromu.utils.Utils

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initView()
    }

    private fun initData() {

    }

    private fun initView() {
        UiUtils.setFullScreenWithStatusBar(this)

        settingBottomNavWithNavController()
    }


    private fun settingBottomNavWithNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        navController = navHostFragment.navController

        binding.bnvMain.setupWithNavController(navController)
    }
}