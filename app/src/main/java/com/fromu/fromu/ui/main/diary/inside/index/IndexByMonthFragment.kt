package com.fromu.fromu.ui.main.diary.inside.index

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.data.dto.IndexDiaryInfo
import com.fromu.fromu.data.remote.network.response.IndexByMonthRes
import com.fromu.fromu.databinding.FragmentIndexByMonthBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.main.diary.inside.InsideDiaryActivity
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.EventObserver
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.IndexInsideDiaryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IndexByMonthFragment : BaseFragment<FragmentIndexByMonthBinding>(FragmentIndexByMonthBinding::inflate) {
    companion object {
        const val INDEX_BY_MONTH_CODE = 100
        const val INDEX_BY_FIRST_PAGE_CODE = 101
        const val INDEX_DIARY_INFO = "indexDiaryInfo"
    }

    private val indexInsideDiaryViewModel: IndexInsideDiaryViewModel by viewModels()
    private lateinit var indexByMonthAdapter: IndexByMonthAdapter

    private var diaryBookId: Int = 0
    private var month: String = ""

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
        settingBundle()
        callApi()
    }

    private fun initView() {

        settingIndexRv()
    }

    private fun settingBundle() {
        arguments?.apply {
            diaryBookId = getInt(IndexMonthFragment.DIARY_BOOK_ID)
            month = getString(IndexMonthFragment.MONTH).toString()
        }
    }

    private fun callApi() {
        indexInsideDiaryViewModel.getDiariesByMonth(diaryBookId, month)
    }

    private fun initObserve() {
        indexInsideDiaryViewModel.indexByMonthListResult.observe(viewLifecycleOwner, EventObserver { resource ->
            handleResource(resource, listener = object : ResourceSuccessListener<IndexByMonthRes> {
                override fun onSuccess(res: IndexByMonthRes) {
                    handleGetIndexByMonthListRes(res)
                }
            })
        })
    }

    private fun initEvent() {
        binding.apply {
            ivIndexBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun settingIndexRv() {
        binding.apply {
            indexByMonthAdapter = IndexByMonthAdapter(object : IndexByMonthAdapter.IndexByMonthAdapterAdapterListener {
                override fun onClick(item: IndexDiaryInfo) {
                    Intent(requireContext(), InsideDiaryActivity::class.java).apply {
                        putExtra(INDEX_DIARY_INFO, item)
                        requireActivity().setResult(INDEX_BY_MONTH_CODE, this)
                        requireActivity().finish()
                    }
                }
            })
            rvIndexInsideDiary.adapter = indexByMonthAdapter
        }
    }


    private fun handleGetIndexByMonthListRes(res: IndexByMonthRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                indexByMonthAdapter.submitList(res.result.diaryInfoList)
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }
}