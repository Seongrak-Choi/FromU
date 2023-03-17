package com.fromu.fromu.ui.main.diary.create

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
import com.fromu.fromu.data.remote.network.response.CreateDiaryBookRes
import com.fromu.fromu.databinding.FragmentDiaryDecideNameBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Extension.debounce
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.CreateDiaryViewModel
import kotlinx.coroutines.launch


class DiaryDecideNameFragment : BaseFragment<FragmentDiaryDecideNameBinding>(FragmentDiaryDecideNameBinding::inflate), Observer<Resource<CreateDiaryBookRes>> {
    private val createDiaryViewModel: CreateDiaryViewModel by activityViewModels()

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
            lifecycleOwner = this@DiaryDecideNameFragment
            vm = createDiaryViewModel
        }
    }

    private fun initEvent() {
        binding.apply {
            etContents.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                vUnderline.isSelected = hasFocus
            }

            // 다이어리 이름 지연 입력창
            etContents.debounce(coroutineScope = lifecycleScope) {
                if (it.isEmpty()) {
                    createDiaryViewModel.isValidDiaryName.value = false
                } else {
                    if (checkPattern(it)) {
                        createDiaryViewModel.isValidDiaryName.value = checkPattern(it)
                        createDiaryViewModel.inputDiaryName.value = it
                        setPassNicknameUi()
                    } else {
                        setNicknameErrorUi()
                    }
                }
            }

            // 다이어리 이름름 입력창
            etContents.doAfterTextChanged {
                createDiaryViewModel.isValidDiaryName.value = false
                setNicknameNormalUi()
            }

            // 완료 버튼
            tvDiaryDecideNameDone.setOnClickListener {
                lifecycleScope.launch {
                    createDiaryViewModel.postDiaryBook().observe(viewLifecycleOwner, this@DiaryDecideNameFragment)
                }
            }

            // 뒤로가기 버튼
            ivDiaryDecideNameBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }


    /**
     * 정규화에 일치하지 않은 경우의 입력 UI 셋팅
     *
     */
    private fun setNicknameErrorUi() {
        binding.apply {
            vUnderline.background = ContextCompat.getDrawable(requireContext(), R.color.color_ff4a6b)
            tvWarringMsg.visibility = View.VISIBLE
            tvWarringMsg.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_ff4a6b))
        }
    }

    /**
     * 정규식에 통과했을 때 입력 UI 셋팅
     *
     */
    private fun setPassNicknameUi() {
        binding.apply {
            vUnderline.background = ContextCompat.getDrawable(requireContext(), R.color.color_dedee2)
            tvWarringMsg.visibility = View.GONE
        }
    }


    /**
     *  일반적인 경우의 입력 UI 셋팅
     *
     */
    private fun setNicknameNormalUi() {
        binding.apply {
            vUnderline.background = ContextCompat.getDrawable(requireContext(), R.color.color_a735ff)
            tvWarringMsg.visibility = View.VISIBLE
            tvWarringMsg.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_6f6f6f))
        }
    }

    private fun handlePostDiaryBook(res: CreateDiaryBookRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                findNavController().navigate(R.id.action_diaryDecideNameFragment_to_diarySuccessCreateLottieFragment)
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
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
        return str.matches(Regex(Const.NO_GAP_EXPRESSION))
    }

    override fun onChanged(resource: Resource<CreateDiaryBookRes>) {
        handleResource(resource, true, object : ResourceSuccessListener<CreateDiaryBookRes> {
            override fun onSuccess(res: CreateDiaryBookRes) {
                handlePostDiaryBook(res)
            }
        })
    }
}