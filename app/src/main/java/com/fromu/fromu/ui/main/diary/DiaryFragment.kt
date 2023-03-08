package com.fromu.fromu.ui.main.diary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.FromUApplication
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.response.DiaryViewRes
import com.fromu.fromu.data.remote.network.response.PatchDiaryBooksPassRes
import com.fromu.fromu.databinding.FragmentDiaryBinding
import com.fromu.fromu.model.DiaryStatusType
import com.fromu.fromu.model.listener.DialogPopupOneBtnListener
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.dialog.DialogPopupOneBtn
import com.fromu.fromu.ui.main.MainActivity
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.DiaryViewModel
import com.fromu.fromu.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiaryFragment : BaseFragment<FragmentDiaryBinding>(FragmentDiaryBinding::inflate) {

    private val diaryViewModel: DiaryViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initData() {
        lifecycleScope.launch {
            diaryViewModel.getDiaryView()
        }
    }

    private fun initView() {
        activity?.let { activity ->
            if (activity is MainActivity) {
                activity.isVisibleBottomNav(true)
            }
        }


        binding.apply {
            lifecycleOwner = this@DiaryFragment
            vm = diaryViewModel
            mainVm = mainViewModel

            clDiaryRoot.setPadding(0, FromUApplication.statusHeight, 0, 0)
        }

        initObserver()
        initEvent()
    }

    private fun initEvent() {
        binding.apply {

            // 디데이 설정 안 했을 때 클릭을 위한 뷰
            vDiaryDday.setOnClickListener {
                findNavController().navigate(R.id.action_diaryFragment_to_firstMetDayFragment)
            }

            //일기장 추가
            ivDiaryAdd.setOnClickListener {
                DialogPopupOneBtn(getString(R.string.diary_popup_no_write), getString(R.string.ok), object : DialogPopupOneBtnListener {
                    override fun onResult() {
                        //Nothing
                    }
                }).show(childFragmentManager, DialogPopupOneBtn.TAG)
//                findNavController().navigate(R.id.action_diaryFragment_to_CreateDiary)
            }

            //일기장 보내기 버튼
            tvDiarySend.setOnClickListener {
                diaryViewModel.patchDiaryBooksPass()
            }
        }
    }

    private fun initObserver() {
        diaryViewModel.apply {

            // 다이어리 뷰 조회 결과
            diaryViewInfo.observe(viewLifecycleOwner) { resource ->
                handleResource(resource, true, object : ResourceSuccessListener<DiaryViewRes> {
                    override fun onSuccess(res: DiaryViewRes) {
                        handleDiaryViewResult(res)
                    }
                })
            }

            // 일기 보내기 결과
            diaryPassResult.observe(viewLifecycleOwner) { resource ->
                handleResource(resource, listener = object : ResourceSuccessListener<PatchDiaryBooksPassRes> {
                    override fun onSuccess(res: PatchDiaryBooksPassRes) {
                        handleDiaryPassResult(res)
                    }
                })
            }
        }
    }


    /**
     * 다이어리 뷰 조회 결과 핸들링
     *
     * @param res
     */
    private fun handleDiaryViewResult(res: DiaryViewRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                val diaryInfo = res.result
                diaryViewModel.apply {
                    dday.value = if (diaryInfo.dday == Const.DDAY_NO_SETTING) "00" else diaryInfo.dday.toString()
                    isSetFirstMetDay.value = diaryInfo.dday != 0
                    partnerNickname.value = diaryInfo.partnerNickname
                    myNickname.value = diaryInfo.nickName
                    diaryBookStatusId.value = diaryInfo.diaryBookStatus.toString()

                    diaryInfo.diaryBook?.let { diary ->
                        diaryName.value = diary.name
                        diaryCoverNum.value = diary.coverNum
                    }
                }
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }

    /**
     * 일기장 전송 결과 핸들링
     *
     * @param res
     */
    private fun handleDiaryPassResult(res: PatchDiaryBooksPassRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                // 화면 새로고침을 할 지 아니면 어차피 보냈으니까 보낸 화면으로 처리할 지 추후 생각해 보자.
                diaryViewModel.diaryBookStatusId.value = DiaryStatusType.GOING.id.toString()
            }
            2074 -> {
                showNoWritePopup()
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }

    /**
     * 일기를 작성하지 않은 경우 보여줄 다이얼로그
     */
    private fun showNoWritePopup() {
        DialogPopupOneBtn(getString(R.string.diary_popup_no_write), getString(R.string.ok), object : DialogPopupOneBtnListener {
            override fun onResult() {
                //Nothing
            }
        }).show(childFragmentManager, DialogPopupOneBtn.TAG)
    }
}