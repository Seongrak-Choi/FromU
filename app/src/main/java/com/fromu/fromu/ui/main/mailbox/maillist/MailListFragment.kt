package com.fromu.fromu.ui.main.mailbox.maillist

import android.os.Bundle
import android.view.View
import com.fromu.fromu.R
import com.fromu.fromu.databinding.FragmentMailListBinding
import com.fromu.fromu.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

class MailListFragment : BaseFragment<FragmentMailListBinding>(FragmentMailListBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initData() {}
    private fun initView() {
        binding.apply {

        }
        settingViewPager2()
        settingTabLayoutWithViewPager2()
    }

    private fun settingViewPager2() {
        binding.apply {
            vpMailList.adapter = MailListMenuVpAdapter(this@MailListFragment)
        }
    }

    private fun settingTabLayoutWithViewPager2() {
        binding.apply {
            TabLayoutMediator(tlMailList, vpMailList) { tab, position ->
                tab.text = resources.getStringArray(R.array.mail_list_tab_menu)[position]
            }.attach()
        }
    }
}