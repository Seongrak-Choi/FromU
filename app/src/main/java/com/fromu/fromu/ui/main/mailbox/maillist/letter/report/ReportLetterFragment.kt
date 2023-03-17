package com.fromu.fromu.ui.main.mailbox.maillist.letter.report

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.ReportLetterRes
import com.fromu.fromu.databinding.FragmentReportLetterBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.ReportLetterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportLetterFragment : BaseFragment<FragmentReportLetterBinding>(FragmentReportLetterBinding::inflate), Observer<Resource<ReportLetterRes>> {

    private val reportLetterViewModel: ReportLetterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initEvent()
    }

    private fun initData() {
        setArgs()
    }

    private fun initView() {
        binding.apply {
            lifecycleOwner = this@ReportLetterFragment
            vm = reportLetterViewModel
        }
    }

    private fun initEvent() {
        binding.apply {
            tvReportLetterSend.setOnClickListener {
                reportLetterViewModel.reportLetter().observe(viewLifecycleOwner, this@ReportLetterFragment)
            }
        }
    }

    private fun setArgs() {
        val args: ReportLetterFragmentArgs by navArgs()
        reportLetterViewModel.letterId.value = args.letterId
    }

    override fun onChanged(resources: Resource<ReportLetterRes>) {
        handleResource(resources, true, object : ResourceSuccessListener<ReportLetterRes> {
            override fun onSuccess(res: ReportLetterRes) {
                when (res.code) {
                    Const.SUCCESS_CODE -> {
                        findNavController().navigate(R.id.action_reportLetterFragment_to_reportLetterSuccessFragment)
                    }
                    else -> {
                        Utils.showNetworkErrorSnackBar(binding.root)
                    }
                }
            }
        })
    }
}