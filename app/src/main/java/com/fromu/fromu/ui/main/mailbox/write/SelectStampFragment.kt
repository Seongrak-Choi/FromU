package com.fromu.fromu.ui.main.mailbox.write

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.data.remote.network.response.StampCountRes
import com.fromu.fromu.databinding.FragmentSelectStampBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.SelectStampViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectStampFragment : BaseFragment<FragmentSelectStampBinding>(FragmentSelectStampBinding::inflate) {
    private val selectStampViewModel: SelectStampViewModel by viewModels()
    private lateinit var selectStampRvAdapter: SelectStampRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObserve()
        initEvent()
    }

    private fun initData() {
        callApi()
    }

    private fun initView() {
        settingStampRv()

        binding.apply {
            lifecycleOwner = this@SelectStampFragment
            vm = selectStampViewModel
        }
    }

    private fun initAdapter() {
        selectStampRvAdapter = SelectStampRvAdapter(object : SelectStampRvAdapter.SelectStampListener {
            override fun onSelect(stampId: Int) {
                selectStampViewModel.selectStampId.value = stampId
            }
        })
    }

    private fun initEvent() {
        binding.apply {
            // 뒤로 버튼
            ivSelectStampBack.setOnClickListener {
                requireActivity().finish()
            }

            // 다음 버튼
            tvSelectStampNext.setOnClickListener {
                selectStampViewModel.selectStampId.value?.let {
                    val action = SelectStampFragmentDirections.actionSelectStampFragmentToWriteLetterFragment(it)
                    findNavController().navigate(action)
                }

            }
        }
    }

    private fun initObserve() {
        selectStampViewModel.getStampCountResult.observe(viewLifecycleOwner) { resources ->
            handleResource(resources, listener = object : ResourceSuccessListener<StampCountRes> {
                override fun onSuccess(res: StampCountRes) {
                    handleStampCountRes(res)
                }
            })
        }
    }

    private fun callApi() {
        selectStampViewModel.getStampList()
    }

    /**
     * 우표 선택하는 리사이클러뷰 어댑터 셋팅
     */
    private fun settingStampRv() {
        binding.rvSelectStamp.adapter = selectStampRvAdapter
    }


    /**
     * 보유한 우표 조회 결과 핸들링
     *
     * @param res
     */
    private fun handleStampCountRes(res: StampCountRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                selectStampViewModel.isPossessStamp.value = res.result.isNotEmpty()
                selectStampRvAdapter.submitList(res.result)
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }
}