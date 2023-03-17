package com.fromu.fromu.ui.main.myhome.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.databinding.FragmentAppInfoBinding
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.utils.Const

class AppInfoFragment : BaseFragment<FragmentAppInfoBinding>(FragmentAppInfoBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initEvent()
    }

    private fun initData() {}
    private fun initView() {}
    private fun initEvent() {
        binding.apply {
            // 이용약관
            clTerms.setOnClickListener {
                Intent(Intent.ACTION_VIEW, Uri.parse(Const.TERMS_OF_USE)).apply {
                    startActivity(this)
                }
            }

            // 개인정보처리방침
            clPersonalInfo.setOnClickListener {
                Intent(Intent.ACTION_VIEW, Uri.parse(Const.PRIVACY_POLICY)).apply {
                    startActivity(this)
                }
            }

            ivAppInfoBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}