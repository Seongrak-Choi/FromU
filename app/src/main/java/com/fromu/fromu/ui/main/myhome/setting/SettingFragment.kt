package com.fromu.fromu.ui.main.myhome.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.FromUApplication
import com.fromu.fromu.data.remote.network.response.BreakMatchingRes
import com.fromu.fromu.data.remote.network.response.LogoutRes
import com.fromu.fromu.data.remote.network.response.WithdrawalRes
import com.fromu.fromu.databinding.FragmentSettingBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.LoginActivity
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.dialog.DialogPopupTwoBtn
import com.fromu.fromu.ui.main.MainActivity
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.MyHomeViewModel

class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {

    private val myHomeViewModel: MyHomeViewModel by activityViewModels()

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

    private fun initData() {}
    private fun initView() {
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).isVisibleBottomNav(false)
        }
    }

    private fun initObserve() {
        myHomeViewModel.logoutResult.observe(viewLifecycleOwner) { resources ->
            handleResource(resources, listener = object : ResourceSuccessListener<LogoutRes> {
                override fun onSuccess(res: LogoutRes) {
                    handleLogoutResult(res)
                }
            })
        }

        myHomeViewModel.breakMatchingResult.observe(viewLifecycleOwner) { resources ->
            handleResource(resources, listener = object : ResourceSuccessListener<BreakMatchingRes> {
                override fun onSuccess(res: BreakMatchingRes) {
                    handleBreakMatchingResult(res)
                }
            })
        }

        myHomeViewModel.withdrawalResult.observe(viewLifecycleOwner) { resources ->
            handleResource(resources, listener = object : ResourceSuccessListener<WithdrawalRes> {
                override fun onSuccess(res: WithdrawalRes) {
                    handleWithdrawalResult(res)
                }
            })

        }
    }

    private fun initEvent() {
        binding.apply {

            // ??????/????????????
            clNoticeInquire.setOnClickListener {
                Intent(Intent.ACTION_VIEW, Uri.parse(Const.FROMU_INSTAGRAM_URL)).apply {
                    startActivity(this)
                }
            }

            // ????????????
            clLogout.setOnClickListener {
                myHomeViewModel.logout()
            }

            // ?????? ?????? ??????
            clBreakMatching.setOnClickListener {
                myHomeViewModel.breakMatching()
            }

            // ????????????
            tvWithdrawal.setOnClickListener {
                DialogPopupTwoBtn("?????? ????????? ????????????...?", "????????????", "????????????", object : DialogPopupTwoBtn.DialogPopupTwoBtnListener {
                    override fun onNegative() {
                        //Nothing
                    }

                    override fun onPositive() {
                        myHomeViewModel.withdrawal()
                    }
                }).show(childFragmentManager, DialogPopupTwoBtn.TAG)
            }

            // ??? ??????
            ivSettingBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    /**
     * ???????????? ?????? ?????????
     *
     * @param res
     */
    private fun handleLogoutResult(res: LogoutRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                deleteUserInfoAndGoLoginActivity()
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }

    /**
     * ?????? ?????? ?????? ?????? ?????????
     *
     * @param res
     */
    private fun handleBreakMatchingResult(res: BreakMatchingRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                deleteUserInfoAndGoLoginActivity()
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }

    /**
     * ???????????? ?????? ?????????
     *
     * @param res
     */
    private fun handleWithdrawalResult(res: WithdrawalRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                deleteUserInfoAndGoLoginActivity()
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }


    /**
     * SharedPreference??? ?????? ?????? ?????? ?????? ??? ????????? Activity??? ??????
     */
    private fun deleteUserInfoAndGoLoginActivity() {
        FromUApplication.prefManager.clearForLogout()
        Intent(requireContext(), LoginActivity::class.java).apply {
            startActivity(this)
        }
        requireActivity().finish()
    }
}