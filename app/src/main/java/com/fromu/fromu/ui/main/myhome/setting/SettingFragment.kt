package com.fromu.fromu.ui.main.myhome.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.FromUApplication
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.response.LogoutRes
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

    private fun initData() {
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).apply {
                isVisibleBottomNav(false)
                isVisibleAppbar(false)
            }
        }
    }

    private fun initView() {}

    private fun initObserve() {
        myHomeViewModel.logoutResult.observe(viewLifecycleOwner) { resources ->
            handleResource(resources, listener = object : ResourceSuccessListener<LogoutRes> {
                override fun onSuccess(res: LogoutRes) {
                    handleLogoutResult(res)
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

            // 앱 정보
            clAppInfo.setOnClickListener {
                findNavController().navigate(R.id.action_settingFragment_to_appInfoFragment)
            }

            // 로그아웃
            clLogout.setOnClickListener {
                DialogPopupTwoBtn(getString(R.string.logout_dialog_contents), getString(R.string.back), getString(R.string.logout_do), object : DialogPopupTwoBtn.DialogPopupTwoBtnListener {
                    override fun onNegative() {
                        //Nothing
                    }

                    override fun onPositive() {
                        myHomeViewModel.logout()
                    }
                }).show(childFragmentManager, DialogPopupTwoBtn.TAG)
            }

            // 커플 연결 끊기
            clBreakMatching.setOnClickListener {
                findNavController().navigate(R.id.action_settingFragment_to_breakMatchingCheckFragment)
            }

            // 탈퇴 하기
            tvWithdrawal.setOnClickListener {
                findNavController().navigate(R.id.action_settingFragment_to_withdrawalCheckFragment)
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