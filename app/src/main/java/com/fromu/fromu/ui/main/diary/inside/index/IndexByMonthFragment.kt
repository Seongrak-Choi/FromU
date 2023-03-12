package com.fromu.fromu.ui.main.diary.inside.index

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.data.remote.network.response.IndexByMonthRes
import com.fromu.fromu.databinding.FragmentIndexByMonthBinding
import com.fromu.fromu.model.IndexInsideDiaryModel
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.EventObserver
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.IndexInsideDiaryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IndexByMonthFragment : BaseFragment<FragmentIndexByMonthBinding>(FragmentIndexByMonthBinding::inflate) {
    companion object {
        const val MONTH = "month"
    }

    private val indexInsideDiaryViewModel: IndexInsideDiaryViewModel by viewModels()
    private lateinit var indexRvAdapter: IndexInsideDiaryAdapter

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
            month = getString(MONTH).toString()
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
            indexRvAdapter = IndexInsideDiaryAdapter(object : IndexInsideDiaryAdapter.IndexDiInsidearyAdapterListener {
                override fun onClickMonth(month: String) {
                    //TODO 해당 일기로 이동
                }
            })
            rvIndexInsideDiary.adapter = indexRvAdapter
        }
    }


    private fun handleGetIndexByMonthListRes(res: IndexByMonthRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                indexRvAdapter.submitList(res.result.diaryInfoList.map { IndexInsideDiaryModel.IndexDay(it) })
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }
}