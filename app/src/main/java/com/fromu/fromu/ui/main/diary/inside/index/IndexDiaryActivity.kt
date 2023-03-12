package com.fromu.fromu.ui.main.diary.inside.index

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fromu.fromu.R
import com.fromu.fromu.databinding.ActivityIndexDiaryBinding
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.ui.main.diary.inside.InsideDiaryActivity
import com.fromu.fromu.utils.UiUtils

class IndexDiaryActivity : BaseActivity<ActivityIndexDiaryBinding>(ActivityIndexDiaryBinding::inflate) {
    companion object {
        const val DIARY_BOOk_ID = "diaryBookId"
    }

    private lateinit var navController: NavController

    //일기장 아이디
    private var diaryBookId: Int = 0

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
    }

    /**
     * Navigation Component 셋팅
     */
    private fun settingNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fcvInsideDiary.id) as NavHostFragment
        navController = navHostFragment.navController


        navController.setGraph(R.navigation.nav_inside_diary, bundleOf(InsideDiaryActivity.DIARY_BOOK_ID_KEY to diaryBookId))
    }

    private fun getIntentData() {
        intent.extras?.let {
            diaryBookId = it.getInt(DIARY_BOOk_ID)
        }
    }

}