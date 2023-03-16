package com.fromu.fromu.ui.main.mailbox.stampbox

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.response.StampCountRes
import com.fromu.fromu.databinding.FragmentStampBoxBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Extension.setThrottleClick
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.StampBoxViewModel

class StampBoxFragment : BaseFragment<FragmentStampBoxBinding>(FragmentStampBoxBinding::inflate) {
    private val stampBoxViewModel: StampBoxViewModel by activityViewModels()
    private lateinit var stampListAdapter: PurchaseStampListRvAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getStampList()
        initView()
        initObserve()
        initEvent()
    }

    private fun initData() {}

    private fun initView() {
        binding.apply {
            lifecycleOwner = this@StampBoxFragment
            vm = stampBoxViewModel
        }

        settingStampListRv()
    }

    private fun initObserve() {
        stampBoxViewModel.apply {
            getStampListResult.observe(viewLifecycleOwner) { resources ->
                handleResource(resources, false, object : ResourceSuccessListener<StampCountRes> {
                    override fun onSuccess(res: StampCountRes) {
                        handleStampListRes(res)
                    }
                })
            }
        }
    }

    private fun initAdapter() {
        stampListAdapter = PurchaseStampListRvAdapter(object : PurchaseStampListRvAdapter.SelectStampListener {
            override fun onSelect(stampId: Int) {
                //Nothing
            }
        })
    }

    private fun initEvent() {
        binding.apply {
            tvStampBoxPurchase.setThrottleClick(lifecycleScope) {
                findNavController().navigate(R.id.action_stampBoxFragment_to_purchaseStampFragment)
            }

            // 뒤로가기
            ivStampBoxBack.setOnClickListener {
                requireActivity().finish()
            }
        }
    }

    /**
     * 보유한 우표 조회 api 호출
     */
    private fun getStampList() {
        stampBoxViewModel.getStampList()
    }

    private fun settingStampListRv() {
        binding.apply {
            rvStampList.adapter = stampListAdapter
        }
    }

    /**
     * 보유한 우표 조회 결과 핸들링
     *
     * @param res
     */
    private fun handleStampListRes(res: StampCountRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                stampBoxViewModel.isPossessStamp.value = res.result.isNotEmpty()
                stampListAdapter.submitList(res.result)
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }
}