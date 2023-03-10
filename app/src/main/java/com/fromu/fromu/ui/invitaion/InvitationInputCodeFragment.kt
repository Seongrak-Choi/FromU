package com.fromu.fromu.ui.invitaion

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.MatchingRes
import com.fromu.fromu.databinding.FragmentInvitationInputCodeBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Extension.debounce
import com.fromu.fromu.utils.Logger
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.InvitationViewModel
import kotlinx.coroutines.launch

class InvitationInputCodeFragment : BaseFragment<FragmentInvitationInputCodeBinding>(FragmentInvitationInputCodeBinding::inflate), Observer<Resource<MatchingRes>> {
    private val invitationViewModel: InvitationViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initData() {}
    private fun initView() {
        binding.apply {
            lifecycleOwner = this@InvitationInputCodeFragment
            vm = invitationViewModel
        }

        initEvent()
    }

    private fun initEvent() {
        binding.apply {
            // 코드 입력창 이벤트
            etContents.apply {
                onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                    vInputCodeUnderline.isSelected = hasFocus
                }

                debounce(coroutineScope = lifecycleScope) {
                    if (it.isEmpty()) {
                        invitationViewModel.isValidOpponentCode.value = false
                    } else {
                        if (checkPattern(it)) {
                            invitationViewModel.isValidOpponentCode.value = true
                            invitationViewModel.opponentCode.value = it
                            setNicknameNormalUi()
                        } else {
                            setNicknameErrorUi()
                        }
                    }
                }

                doAfterTextChanged {
                    invitationViewModel.isValidOpponentCode.value = false
                    setNicknameNormalUi()
                }
            }

            // 뒤로가기 버튼
            ivInputCodeBack.setOnClickListener {
                findNavController().popBackStack()
            }


            // 연결하기 버튼
            tvInputCodeConnect.setOnClickListener {
                lifecycleScope.launch {
                    invitationViewModel.postMatching().observe(viewLifecycleOwner, this@InvitationInputCodeFragment)
                }
            }
        }
    }


    /**
     * 입력한 닉네임 정규화 확인 메소드
     *
     * @param str
     * @return
     */
    private fun checkPattern(str: String): Boolean {
        return str.matches(Regex(Const.NO_SPECIAL_CHAR_AND_NO_GAP_EXPRESSION)) && str.length == Const.INVITATION_CODE_LENGTH
    }

    /**
     * 정규화에 일치하지 않은 경우의 입력 UI 셋팅
     *
     */
    private fun setNicknameErrorUi() {
        binding.apply {
            vInputCodeUnderline.background = ContextCompat.getDrawable(requireContext(), R.color.color_ff4a6b)
            tvWarringMsg.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_ff4a6b))
        }
    }

    /**
     *  일반적인 경우의 입력 UI 셋팅
     *
     */
    private fun setNicknameNormalUi() {
        binding.apply {
            vInputCodeUnderline.background = ContextCompat.getDrawable(requireContext(), R.color.color_a735ff)
            tvWarringMsg.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_6f6f6f))
        }
    }

    /**
     * 코드 연결 결과 핸들링
     *
     * @param res
     */
    private fun handleMatchingRes(res: MatchingRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                invitationViewModel.opponentNickname.value = res.result.partnerNickname
                findNavController().navigate(R.id.action_invitationInputCodeFragment_to_matchSuccessLottieFragment)

            }
            2020 -> {
                Utils.showCustomSnackBar(binding.root, getString(R.string.invitation_already_exist_msg))
            }
            4001 -> {
                Utils.showCustomSnackBar(binding.root, getString(R.string.invitation_not_exist_msg))
            }
            else -> {
                Logger.e("handleMatching", res.message.toString())
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }

    override fun onChanged(resource: Resource<MatchingRes>) {
        handleResource(resource, true, object : ResourceSuccessListener<MatchingRes> {
            override fun onSuccess(res: MatchingRes) {
                handleMatchingRes(res)
            }
        })
    }
}