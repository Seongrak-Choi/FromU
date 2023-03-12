package com.fromu.fromu.ui.main.diary.inside

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.fromu.fromu.data.remote.network.response.GetMonthListRes
import com.fromu.fromu.databinding.FragmentIndexInsideDiaryBinding
import com.fromu.fromu.model.IndexInsideDiaryModel
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.EventObserver
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.IndexInsideDiaryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IndexInsideDiaryFragment : BaseFragment<FragmentIndexInsideDiaryBinding>(FragmentIndexInsideDiaryBinding::inflate) {

    companion object {
        const val DIARY_BOOK_ID = "diaryBookId"
    }

    private val indexInsideDiaryViewModel: IndexInsideDiaryViewModel by viewModels()
    private val indexRvAdapter = IndexInsideDiaryAdapter()

    private var diaryBookId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initData() {
        settingBundle()
        callApi()
        initObserve()
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
        indexInsideDiaryViewModel.apply {
            getMonthListResult.observe(viewLifecycleOwner, EventObserver { resource ->
                handleResource(resource, listener = object : ResourceSuccessListener<GetMonthListRes> {
                    override fun onSuccess(res: GetMonthListRes) {
                        handleGetMonthListRes(res)
                    }
                })
            })
        }
    }

    private fun settingIndexRv() {
        binding.apply {
            rvIndexInsideDiary.adapter = indexRvAdapter
        }
    }


    private fun handleGetMonthListRes(res: GetMonthListRes) {
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