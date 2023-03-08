package com.fromu.fromu.ui.invitaion

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.FromUApplication
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.response.CheckMatchingRes
import com.fromu.fromu.data.remote.network.response.UserInfoRes
import com.fromu.fromu.databinding.FragmentInvitationMainBinding
import com.fromu.fromu.model.listener.DynamicLinkListener
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.utils.*
import com.fromu.fromu.utils.Extension.setThrottleClick
import com.fromu.fromu.viewmodels.InvitationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class InvitationMainFragment : BaseFragment<FragmentInvitationMainBinding>(FragmentInvitationMainBinding::inflate) {

    private val invitationViewModel: InvitationViewModel by activityViewModels()

    @Inject
    lateinit var dynamicLinkUtil: DynamicLinkUtil


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initData() {
        // 디스크립션 화면 셋팅
        invitationViewModel.setWhetherFirstDescription()

        lifecycleScope.launch {
            FromUApplication.prefManager.getUserId()?.let { userId ->
                invitationViewModel.getUserInfo(userId)
            }
        }
    }

    private fun initView() {
        binding.apply {
            lifecycleOwner = this@InvitationMainFragment
            vm = invitationViewModel
        }

        initEvent()
        initObserve()
    }

    private fun initEvent() {
        binding.apply {
            // x 버튼
            ivDescriptionClose.setOnClickListener {
                invitationViewModel.isShowDescription.value = false
                invitationViewModel.isVisibleRefreshBtn.value = true
                FromUApplication.prefManager.editor.putBoolean(PrefManager.WHETHER_SHOW_INVITATION_DESCRIPTION, false).apply()
            }

            // 새로고침 버튼
            ivInvitationRefresh.setThrottleClick(lifecycleScope) {
                lifecycleScope.launch {
                    invitationViewModel.getCheckMatching()
                }
            }

            // 초대장 보내기 버튼
            tvInvitationSend.setThrottleClick(lifecycleScope) {
                showLoadingDialog()

                dynamicLinkUtil.apply {
                    getDynamicLink(requireActivity(), getInvitationDeepLink(invitationViewModel.myCode.value), object : DynamicLinkListener {
                        override fun onSuccess(deepLink: String) {
                            dismissLoadingDialog()

                            Utils.sendString(
                                requireActivity(),
                                getString(R.string.invitation_send_msg).format(invitationViewModel.myNickname.value, invitationViewModel.myCode.value, deepLink)
                            )
                        }

                        override fun onFailure() {
                            dismissLoadingDialog()
                            Logger.e("dynamicLink", "다이나믹 링크 생성 실패")
                        }
                    })
                }
            }

            tvInvitationInputCode.setOnClickListener {
                findNavController().navigate(R.id.action_invitationMainFragment_to_invitationInputCodeFragment)
            }
        }
    }

    private fun initObserve() {
        invitationViewModel.userInfo.observe(viewLifecycleOwner) { resource ->
            handleResource(resource, true, listener = object : ResourceSuccessListener<UserInfoRes> {
                override fun onSuccess(res: UserInfoRes) {
                    handleUserInfoRes(res)
                }
            })
        }

        invitationViewModel.isMatching.observe(viewLifecycleOwner) { resource ->
            handleResource(resource, listener = object : ResourceSuccessListener<CheckMatchingRes> {
                override fun onSuccess(res: CheckMatchingRes) {
                    handleCheckMatching(res)
                }
            })
        }
    }


    /**
     * 유저 정보 response 핸들링
     *
     * @param res : UserInfoRes
     */
    private fun handleUserInfoRes(res: UserInfoRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                invitationViewModel.myCode.value = res.result.userCode
                invitationViewModel.myNickname.value = res.result.nickname
            }
            else -> {
                Logger.e("checkMatching", res.message.toString())
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }

    /**
     * 커플 매칭 response 핸들링
     *
     * @param res : CheckMatchingRes
     */
    private fun handleCheckMatching(res: CheckMatchingRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                if (res.result.isMatch) {
                    //TODO MainActivity로 이동
                } else {
                    Utils.showCustomSnackBar(binding.root, getString(R.string.invitation_matching_not_yet))
                }
            }
            else -> {
                Logger.e("checkMatching", res.message.toString())
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }
}