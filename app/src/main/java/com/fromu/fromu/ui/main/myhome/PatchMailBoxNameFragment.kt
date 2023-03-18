package com.fromu.fromu.ui.main.myhome

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.PatchMailBoxNameReq
import com.fromu.fromu.data.remote.network.response.PatchMailBoxNameRes
import com.fromu.fromu.databinding.FragmentPatchMailBoxNameBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.main.MainActivity
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Extension.debounce
import com.fromu.fromu.utils.Extension.setThrottleClick
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.DecideMailBoxNameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.ceil

@AndroidEntryPoint
class PatchMailBoxNameFragment : BaseFragment<FragmentPatchMailBoxNameBinding>(FragmentPatchMailBoxNameBinding::inflate), Observer<Resource<PatchMailBoxNameRes>> {
    private val decideMailBoxNameViewModel: DecideMailBoxNameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initData() {
        if (requireActivity() is MainActivity) {
            (requireActivity() as MainActivity).apply {
                isVisibleAppbar(false)
                isVisibleBottomNav(false)
            }
        }
    }

    private fun initView() {
        binding.apply {
            lifecycleOwner = this@PatchMailBoxNameFragment
            vm = decideMailBoxNameViewModel
        }

        initEvent()
    }

    private fun initEvent() {
        binding.apply {

            etContents.apply {
                onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                    vPatchMailBoxUnderline.isSelected = hasFocus
                }

                debounce(coroutineScope = lifecycleScope) {
                    if (it.isEmpty()) {
                        decideMailBoxNameViewModel.isInvalidMailBoxName.value = false
                    } else {
                        if (checkPattern(it)) {
                            decideMailBoxNameViewModel.isInvalidMailBoxName.value = true
                            setNicknameNormalUi()
                        } else {
                            decideMailBoxNameViewModel.isInvalidMailBoxName.value = false
                            setNicknameErrorUi(getString(R.string.decide_mail_box_name_invalid_error_msg))
                        }
                    }
                }

                doAfterTextChanged {
                    if (it.toString().isEmpty()) {
                        tvPatchMailBoxContextSuffix.visibility = View.GONE
                        setResizeEditTextWidthByTextWidth(this, getString(R.string.decide_mail_box_name_hint)) //hint 길이 만큼 width 설정
                    } else {
                        tvPatchMailBoxContextSuffix.visibility = View.VISIBLE
                        setResizeEditTextWidthByTextWidth(this, it.toString())
                    }

                    decideMailBoxNameViewModel.isInvalidMailBoxName.value = false

                    invalidate()
                    setNicknameNormalUi()
                }
            }


            // 결정하기 버튼
            tvPatchMailBoxConnect.setThrottleClick(lifecycleScope) {
                lifecycleScope.launch {
                    decideMailBoxNameViewModel.patchMailBoxName(PatchMailBoxNameReq("${etContents.text.toString()}함")).observe(viewLifecycleOwner, this@PatchMailBoxNameFragment)
                }
            }

            ivPatchMailBoxBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    /**
     * 입력한 Text의 길이만큼 width를 다시 그리는 메소드
     *
     * @param editText
     * @param text
     */
    private fun setResizeEditTextWidthByTextWidth(editText: EditText, text: String) {
        val textWidth: Float = editText.paint.measureText(text)
        val width = ceil(textWidth.toDouble()).toInt()
        val param = LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT)
        editText.layoutParams = param
    }

    /**
     * 입력한 닉네임 정규화 확인 메소드
     *
     * @param str
     * @return
     */
    private fun checkPattern(str: String): Boolean {
        return str.matches(Regex(Const.NO_SPECIAL_CHAR_AND_NO_GAP_EXPRESSION))
    }


    /**
     * 정규식에 통과했을 때 입력 UI 셋팅
     *
     */
    private fun setPassNicknameUi() {
        binding.apply {
            vPatchMailBoxUnderline.background = ContextCompat.getDrawable(requireContext(), R.color.color_dedee2)
            tvWarringMsg.visibility = View.GONE
        }
    }


    /**
     * 정규화에 일치하지 않은 경우의 입력 UI 셋팅
     *
     */
    private fun setNicknameErrorUi(errorMsg: String) {
        binding.apply {
            vPatchMailBoxUnderline.background = ContextCompat.getDrawable(requireContext(), R.color.color_ff4a6b)
            tvWarringMsg.visibility = View.VISIBLE
            tvWarringMsg.text = errorMsg
            tvWarringMsg.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_ff4a6b))
        }
    }


    /**
     *  일반적인 경우의 입력 UI 셋팅
     *
     */
    private fun setNicknameNormalUi() {
        binding.apply {
            vPatchMailBoxUnderline.background = ContextCompat.getDrawable(requireContext(), R.color.color_a735ff)
            tvWarringMsg.visibility = View.VISIBLE
            tvWarringMsg.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_6f6f6f))
        }
    }

    private fun handlePatchMailBoxNameRes(res: PatchMailBoxNameRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                findNavController().popBackStack()
            }
            2062 -> {
                setNicknameErrorUi(getString(R.string.decide_mail_box_name_duplication_error_msg))
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }


    override fun onChanged(resource: Resource<PatchMailBoxNameRes>) {
        handleResource(resource, true, listener = object : ResourceSuccessListener<PatchMailBoxNameRes> {
            override fun onSuccess(res: PatchMailBoxNameRes) {
                handlePatchMailBoxNameRes(res)
            }
        })
    }
}