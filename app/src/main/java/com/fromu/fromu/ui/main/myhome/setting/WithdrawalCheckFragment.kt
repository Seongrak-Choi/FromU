package com.fromu.fromu.ui.main.myhome.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.FromUApplication
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.response.WithdrawalRes
import com.fromu.fromu.databinding.FragmentWithdrawalCheckBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.LoginActivity
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.dialog.DialogPopupTwoBtn
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.MyHomeViewModel

class WithdrawalCheckFragment : BaseFragment<FragmentWithdrawalCheckBinding>(FragmentWithdrawalCheckBinding::inflate) {
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

    }

    private fun initView() {}

    private fun initEvent() {
        binding.apply {

            // 탈퇴 하기
            tvWithdrawalDisconnect.setOnClickListener {
                showWithdrawalDialog()
            }

            // 함께하기
            tvWithdrawalContinue.setOnClickListener {
                findNavController().popBackStack()
            }

            // 뒤로가기
            ivWithdrawalBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initObserve() {
        myHomeViewModel.withdrawalResult.observe(viewLifecycleOwner) { resources ->
            handleResource(resources, listener = object : ResourceSuccessListener<WithdrawalRes> {
                override fun onSuccess(res: WithdrawalRes) {
                    handleWithdrawalResult(res)
                }
            })
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


    /**
     * 연결 끊기 확인 다이얼로그 출력
     */
    private fun showWithdrawalDialog() {
        DialogPopupTwoBtn(getString(R.string.withdrawal_dialog_contents), getString(R.string.withdrawal), getString(R.string.break_matching_continue), object : DialogPopupTwoBtn.DialogPopupTwoBtnListener {
            override fun onNegative() {
                // 탈퇴하기
                myHomeViewModel.withdrawal()
            }

            override fun onPositive() {
                //Nothing
            }
        }).show(childFragmentManager, DialogPopupTwoBtn.TAG)
    }
}