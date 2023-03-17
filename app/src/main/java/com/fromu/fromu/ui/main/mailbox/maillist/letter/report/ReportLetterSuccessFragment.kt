package com.fromu.fromu.ui.main.mailbox.maillist.letter.report

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.databinding.FragmentReportLetterSuccessBinding
import com.fromu.fromu.ui.base.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ReportLetterSuccessFragment : BaseFragment<FragmentReportLetterSuccessBinding>(FragmentReportLetterSuccessBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initData() {
        goBackAfter1500ms()
    }

    private fun initView() {}

    private fun goBackAfter1500ms() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            findNavController().popBackStack()
        }
    }
}