package com.fromu.fromu.ui.main.mailbox.maillist

import android.os.Bundle
import android.view.View
import com.fromu.fromu.databinding.FragmentReceiveMailListBinding
import com.fromu.fromu.ui.base.BaseFragment

class ReceiveMailListFragment : BaseFragment<FragmentReceiveMailListBinding>(FragmentReceiveMailListBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initData() {}
    private fun initView() {}
}