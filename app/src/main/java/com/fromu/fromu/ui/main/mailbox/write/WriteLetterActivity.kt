package com.fromu.fromu.ui.main.mailbox.write

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fromu.fromu.databinding.ActivityWriteLetterBinding
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.utils.UiUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteLetterActivity : BaseActivity<ActivityWriteLetterBinding>(ActivityWriteLetterBinding::inflate) {
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
        setWindowAdjustResize()
    }

    /**
     * Navigation Component 셋팅
     */
    private fun settingNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fcvWriteLetter.id) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setWindowAdjustResize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            binding.root.setOnApplyWindowInsetsListener { _, insets ->
                val topInset = insets.getInsets(WindowInsets.Type.statusBars()).top
                val imeHeight = insets.getInsets(WindowInsets.Type.ime()).bottom
                val navigationHeight = insets.getInsets(WindowInsets.Type.navigationBars()).bottom
                val bottomInset = if (imeHeight == 0) navigationHeight else imeHeight
                binding.root.setPadding(0, 0, 0, bottomInset)
                insets
            }
        } else {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }
}