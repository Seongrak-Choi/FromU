package com.fromu.fromu.ui.main.myhome.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.FromUApplication
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.response.BreakMatchingRes
import com.fromu.fromu.databinding.FragmentBreakMatchingCheckBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.LoginActivity
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.dialog.DialogPopupTwoBtn
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.MyHomeViewModel

class BreakMatchingCheckFragment : BaseFragment<FragmentBreakMatchingCheckBinding>(FragmentBreakMatchingCheckBinding::inflate) {
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

    private fun initEvent() {
        binding.apply {

            // 연결 끊기
            tvBreakMatchingDisconnect.setOnClickListener {
                showWithdrawalDialog()
            }

            // 함께하기
            tvBreakMatchingContinue.setOnClickListener {
                findNavController().popBackStack()
            }

            // 뒤로가기
            ivBreakMatchingBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initObserve() {
        myHomeViewModel.breakMatchingResult.observe(viewLifecycleOwner) { resources ->
            handleResource(resources, listener = object : ResourceSuccessListener<BreakMatchingRes> {
                override fun onSuccess(res: BreakMatchingRes) {
                    handleBreakMatchingResult(res)
                }
            })
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
        DialogPopupTwoBtn(getString(R.string.break_matching_dialog_contents), getString(R.string.break_matching_disconnect), getString(R.string.break_matching_continue), object : DialogPopupTwoBtn.DialogPopupTwoBtnListener {
            override fun onNegative() {
                myHomeViewModel.breakMatching()
            }

            override fun onPositive() {
                //Nothing
            }
        }).show(childFragmentManager, DialogPopupTwoBtn.TAG)
    }
}