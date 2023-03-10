package com.fromu.fromu.ui.main.diary.inside

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fromu.fromu.R
import com.fromu.fromu.data.dto.DiaryBook
import com.fromu.fromu.databinding.ActivityInsideDiaryBinding
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.utils.Extension.customGetSerializable
import com.fromu.fromu.utils.UiUtils
import com.fromu.fromu.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InsideDiaryActivity : BaseActivity<ActivityInsideDiaryBinding>(ActivityInsideDiaryBinding::inflate) {

    companion object {
        const val DIARY_BOOK_ID_KEY = "diaryBookId"
    }

    private lateinit var navController: NavController

    //일기장 아이디
    private var diaryBookInfo: DiaryBook? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initView()
    }

    private fun initData() {
        getIntentData()
    }

    private fun initView() {
        UiUtils.setFullScreenWithStatusBar(this)
        settingNavController()
        setWindowAdjustResize()
    }


    /**
     * Navigation Component 셋팅
     */
    private fun settingNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fcvInsideDiary.id) as NavHostFragment
        navController = navHostFragment.navController

        diaryBookInfo?.let {
            navController.setGraph(R.navigation.nav_inside_diary, bundleOf(DIARY_BOOK_ID_KEY to diaryBookInfo))
        } ?: let {
            Utils.showNetworkErrorSnackBar(binding.root)
        }
    }

    private fun getIntentData() {
        intent.extras?.let {
            diaryBookInfo = it.customGetSerializable(DIARY_BOOK_ID_KEY)
        }
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