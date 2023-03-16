package com.fromu.fromu.ui.main.mailbox.maillist.letter.rate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.response.RateLetterRes
import com.fromu.fromu.databinding.FragmentRateLetterBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.dialog.DialogPopupTwoBtn
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.RateLetterViewModel

class RateLetterFragment : BaseFragment<FragmentRateLetterBinding>(FragmentRateLetterBinding::inflate) {

    private val rateLetterViewModel: RateLetterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObserve()
        initEvent()
    }

    private fun initData() {
        getArgs()
    }

    private fun initView() {
        binding.apply {
            lifecycleOwner = this@RateLetterFragment
            vm = rateLetterViewModel
        }
    }

    private fun initObserve() {
        rateLetterViewModel.apply {
            rateLetterResult.observe(viewLifecycleOwner) { resources ->
                handleResource(resources, listener = object : ResourceSuccessListener<RateLetterRes> {
                    override fun onSuccess(res: RateLetterRes) {
                        handleRateLetterRes(res)
                    }
                })
            }
        }
    }

    private fun initEvent() {
        binding.apply {
            tvRateLetterSend.setOnClickListener {
                DialogPopupTwoBtn(getString(R.string.rate_send_check), getString(R.string.back), getString(R.string.send), object : DialogPopupTwoBtn.DialogPopupTwoBtnListener {
                    override fun onNegative() {
                        // Nothing
                    }

                    override fun onPositive() {
                        rateLetterViewModel.rateLetter()
                    }
                }).show(childFragmentManager, DialogPopupTwoBtn.TAG)
            }
        }
    }

    private fun getArgs() {
        val args: RateLetterFragmentArgs by navArgs()
        rateLetterViewModel.letterId.value = args.letterId
    }

    /**
     * 별점 보내기 결과 핸들링
     *
     * @param res
     */
    private fun handleRateLetterRes(res: RateLetterRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                findNavController().popBackStack()
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }
}