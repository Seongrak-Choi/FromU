package com.fromu.fromu.ui.main.mailbox.maillist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fromu.fromu.databinding.ActivityMailListBinding
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.utils.UiUtils
import com.fromu.fromu.viewmodels.MailListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MailListActivity : BaseActivity<ActivityMailListBinding>(ActivityMailListBinding::inflate) {

    private val mailListViewModel: MailListViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initView()
    }

    private fun initData() {}
    private fun initView() {
        UiUtils.setFullScreenWithStatusBar(this)
        settingNavController()
    }

    /**
     * Navigation Component 셋팅
     */
    private fun settingNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fcvMailList.id) as NavHostFragment
        navController = navHostFragment.navController
    }
}