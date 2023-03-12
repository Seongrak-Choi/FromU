package com.fromu.fromu.ui.main.diary.inside.index

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.response.IndexMonthListRes
import com.fromu.fromu.databinding.FragmentIndexMonthBinding
import com.fromu.fromu.model.IndexInsideDiaryModel
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.EventObserver
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.IndexInsideDiaryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IndexMonthFragment : BaseFragment<FragmentIndexMonthBinding>(FragmentIndexMonthBinding::inflate) {
    companion object {
        const val DIARY_BOOK_ID = "diaryBookId"
    }

    private val indexInsideDiaryViewModel: IndexInsideDiaryViewModel by viewModels()
    private lateinit var indexRvAdapter: IndexInsideDiaryAdapter

    private var diaryBookId: Int = 0


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
        indexRvAdapter = IndexInsideDiaryAdapter(object : IndexInsideDiaryAdapter.IndexDiInsidearyAdapterListener {
            override fun onClickMonth(month: String) {
                val bundle = bundleOf(DIARY_BOOK_ID to diaryBookId, IndexByMonthFragment.MONTH to month)
                findNavController().navigate(R.id.action_indexMonthFragment_to_indexByMonthFragment, bundle)
            }
        })

        settingBundle()
        callApi()
    }

    private fun initView() {

        settingIndexRv()
    }

    private fun settingBundle() {
        arguments?.apply {
            diaryBookId = getInt(DIARY_BOOK_ID)
        }
    }

    private fun callApi() {
        indexInsideDiaryViewModel.getMonthList(diaryBookId)
    }

    private fun initObserve() {
        indexInsideDiaryViewModel.indexMonthListResult.observe(viewLifecycleOwner, EventObserver { resource ->
            handleResource(resource, listener = object : ResourceSuccessListener<IndexMonthListRes> {
                override fun onSuccess(res: IndexMonthListRes) {
                    handleGetMonthListRes(res)
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
            rvIndexInsideDiary.adapter = indexRvAdapter
        }
    }


    private fun handleGetMonthListRes(res: IndexMonthListRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                indexRvAdapter.submitList(res.result.map { IndexInsideDiaryModel.IndexMonth(it) })
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }
}