package com.fromu.fromu.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.fromu.fromu.FromUApplication
import com.fromu.fromu.R
import com.fromu.fromu.databinding.ActivityMainBinding
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.utils.UiUtils
import com.fromu.fromu.viewmodels.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private lateinit var navController: NavController
    private val mainViewModel: MainViewModel by viewModels()

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

        binding.apply {
            lifecycleOwner = this@MainActivity
            vm = mainViewModel
            clMainRoot.setPadding(0, FromUApplication.statusHeight, 0, 0)
        }
    }


    /**
     * bottomNav와 navigation component 셋팅
     */
    private fun settingBottomNavWithNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        navController = navHostFragment.navController

        binding.bnvMain.setupWithNavController(navController)
    }

    /**
     * 바텀네비게이션 visible 핸들
     */
    fun setVisibleBottomNav(isVisible: Boolean) {
        binding.bnvMain.visibility = if (isVisible) View.VISIBLE else View.GONE
    }


    /**
     * appbar visible 핸들
     */
    fun setVisibleAppbar(isVisible: Boolean) {
        binding.layoutAppbar.mainAppbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}