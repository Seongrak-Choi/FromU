package com.fromu.fromu.ui.signup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.FromUApplication
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.SignupRes
import com.fromu.fromu.databinding.FragmentTermsBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.invitaion.InvitationActivity
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Extension.setThrottleClick
import com.fromu.fromu.utils.Logger
import com.fromu.fromu.utils.UiUtils
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.SignupViewModel
import kotlinx.coroutines.launch

class TermsFragment : BaseFragment<FragmentTermsBinding>(FragmentTermsBinding::inflate), Observer<Resource<SignupRes>> {

    private val signupViewModel: SignupViewModel by activityViewModels()

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
    private fun initView() {
        binding.apply {
            lifecycleOwner = this@TermsFragment
            vm = signupViewModel


            UiUtils.delayShowText(getString(R.string.terms_title).format(signupViewModel.nickname.value), tvTermsTitle, 250)
        }
    }

    private fun initEvent() {
        binding.apply {
            // 이용약관
            tvTermsOfUser.setOnClickListener {
                Intent(Intent.ACTION_VIEW, Uri.parse(Const.TERMS_OF_USE)).apply {
                    startActivity(this)
                }
            }

            // 개인정보수집
            tvPersonalInformation.setOnClickListener {
                Intent(Intent.ACTION_VIEW, Uri.parse(Const.PRIVACY_POLICY)).apply {
                    startActivity(this)
                }
            }

            // '동의하고 시작하기' 버튼
            tvBirthdayNext.setThrottleClick(lifecycleScope) {
                lifecycleScope.launch {
                    signupViewModel.postSignup().observe(viewLifecycleOwner, this@TermsFragment)
                }
            }

            // 뒤로가기 버튼
            ivTemrsBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }


    private fun handleSignupResult(signupRes: SignupRes) {
        when (signupRes.code) {
            Const.SUCCESS_CODE -> {
                FromUApplication.prefManager.setUserId(signupRes.result.userId)
                //invitation activity로 이동
                Intent(requireContext(), InvitationActivity::class.java).apply {
                    startActivity(this)
                }
                requireActivity().finish()
            }
            else -> {
                Logger.e("signupResult", signupRes.message ?: "회원가입 실패")
                Utils.showCustomSnackBar(binding.root, "회원가입에 실패하였습니다")
            }
        }
    }

    override fun onChanged(resource: Resource<SignupRes>) {
        handleResource(resource, true, listener = object : ResourceSuccessListener<SignupRes> {
            override fun onSuccess(res: SignupRes) {
                handleSignupResult(res)
            }
        })
    }
}