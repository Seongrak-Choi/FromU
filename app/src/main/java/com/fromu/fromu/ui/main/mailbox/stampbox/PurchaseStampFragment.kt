package com.fromu.fromu.ui.main.mailbox.stampbox

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.response.FromCountRes
import com.fromu.fromu.data.remote.network.response.PurchaseStampRes
import com.fromu.fromu.databinding.FragmentPurchaseStampBinding
import com.fromu.fromu.model.FindStamp
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.dialog.DialogPopupStampPurchase
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.EventObserver
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.StampBoxViewModel

class PurchaseStampFragment : BaseFragment<FragmentPurchaseStampBinding>(FragmentPurchaseStampBinding::inflate) {

    private val stampBoxViewModel: StampBoxViewModel by activityViewModels()
    private lateinit var stampListAdapter: PurchaseStampListRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initEvent()
        initObserve()
    }

    private fun initData() {}
    private fun initView() {
        settingStampListRv()

        binding.apply {
            lifecycleOwner = this@PurchaseStampFragment
            vm = stampBoxViewModel
        }
    }

    private fun initAdapter() {
        stampListAdapter = PurchaseStampListRvAdapter(object : PurchaseStampListRvAdapter.SelectStampListener {
            override fun onSelect(stampId: Int) {
                showDialogPurchaseTamp(stampId)
            }
        })
    }

    private fun initEvent() {
        binding.apply {
            // 뒤로가기
            ivPurchaseStampBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initObserve() {
        stampBoxViewModel.purchaseStampResult.observe(viewLifecycleOwner, EventObserver { resources ->
            handleResource(resources, true, object : ResourceSuccessListener<PurchaseStampRes> {
                override fun onSuccess(res: PurchaseStampRes) {
                    handlePurchaseRes(res)
                }
            })
        })

        stampBoxViewModel.getFromCountResult.observe(viewLifecycleOwner) { resources ->
            handleResource(resources, false, object : ResourceSuccessListener<FromCountRes> {
                override fun onSuccess(res: FromCountRes) {
                    handleGetFromCountRes(res)
                }
            })
        }
    }

    private fun settingStampListRv() {
        binding.apply {
            rvPurchaseStampList.adapter = stampListAdapter
            stampListAdapter.submitList(FindStamp.getStampList().map { it.stampId }.subList(1, 7))
        }
    }


    /**
     * 우표 설명 다이얼로그 노출
     *
     * @param stampId
     */
    private fun showDialogPurchaseTamp(stampId: Int) {
        DialogPopupStampPurchase(stampId, object : DialogPopupStampPurchase.DialogPopupStampPurchaseListener {
            override fun onNegative() {
                //Nothing
            }

            override fun onPositive() {
                if (checkEnoughFrom(FindStamp.getStampPriceById(stampId))) {
                    stampBoxViewModel.purchaseStamp(stampId)
                } else {
                    Utils.showBigCustomSnackBar(binding.root, getString(R.string.lack_from))
                }
            }
        }).show(childFragmentManager, DialogPopupStampPurchase.TAG)
    }


    /**
     * 우표 구매 결과 핸들링
     *
     * @param res
     */
    private fun handlePurchaseRes(res: PurchaseStampRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                Utils.showBigCustomSnackBar(binding.root, getString(R.string.success_purchase_stamp))
                stampBoxViewModel.getFromCount()
            }
            3061 -> {
                //프롬 부족
                Utils.showBigCustomSnackBar(binding.root, getString(R.string.lack_from))
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }

    /**
     * 보유 우표 조회 결과 핸들링
     *
     * @param res
     */
    private fun handleGetFromCountRes(res: FromCountRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                stampBoxViewModel.fromCountFlow.value = res.result
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }


    /**
     * 우표 구매를 위한 api 호출 전 충분한 프롬이 있는지 확인
     *
     * @param stampPrice 우표 가격
     * @return
     */
    private fun checkEnoughFrom(stampPrice: Int): Boolean {
        return stampBoxViewModel.fromCountFlow.value >= stampPrice
    }
}