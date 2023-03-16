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
    private fun initView() {}

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

            // 공지/문의하기
            clNoticeInquire.setOnClickListener {
                Intent(Intent.ACTION_VIEW, Uri.parse(Const.FROMU_INSTAGRAM_URL)).apply {
                    startActivity(this)
                }
            }

            // 로그아웃
            clLogout.setOnClickListener {
                myHomeViewModel.logout()
            }

            // 커플 연결 끊기
            clBreakMatching.setOnClickListener {
                myHomeViewModel.breakMatching()
            }

            // 탈퇴하기
            tvWithdrawal.setOnClickListener {
                DialogPopupTwoBtn("정말 우리를 떠날거야...?", "돌아가기", "탈퇴하기", object : DialogPopupTwoBtn.DialogPopupTwoBtnListener {
                    override fun onNegative() {
                        //Nothing
                    }

                    override fun onPositive() {
                        myHomeViewModel.withdrawal()
                    }
                }).show(childFragmentManager, DialogPopupTwoBtn.TAG)
            }

            // 백 버튼
            ivSettingBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    /**
     * 로그아웃 결과 핸들링
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
     * 커플 연결 끊기 결과 핸들링
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
     * 탈퇴하기 결과 핸들링
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
     * SharedPreference에 있는 유저 정보 삭제 후 로그인 Activity로 이동
     */
    private fun deleteUserInfoAndGoLoginActivity() {
        FromUApplication.prefManager.clearForLogout()
        Intent(requireContext(), LoginActivity::class.java).apply {
            startActivity(this)
        }
        requireActivity().finish()
    }
}