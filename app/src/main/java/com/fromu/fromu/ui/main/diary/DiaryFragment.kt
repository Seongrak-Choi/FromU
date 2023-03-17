package com.fromu.fromu.ui.main.diary

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.response.DiaryViewRes
import com.fromu.fromu.data.remote.network.response.PushPartnerRes
import com.fromu.fromu.data.remote.network.response.SendDiaryBooksRes
import com.fromu.fromu.databinding.FragmentDiaryBinding
import com.fromu.fromu.model.DiaryStatusType
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.dialog.DialogPopupOneBtn
import com.fromu.fromu.ui.main.MainActivity
import com.fromu.fromu.ui.main.diary.inside.InsideDiaryActivity
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.EventObserver
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.DiaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiaryFragment : BaseFragment<FragmentDiaryBinding>(FragmentDiaryBinding::inflate) {

    private val diaryViewModel: DiaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initApi()
        initView()
        initObserve()
        initEvent()
    }

    private fun initData() {

    }

    private fun initView() {
        activity?.let { activity ->
            if (activity is MainActivity) {
                activity.isVisibleBottomNav(true)
                activity.isVisibleAppbar(true)
            }
        }

        binding.apply {
            lifecycleOwner = this@DiaryFragment
            vm = diaryViewModel
        }
    }

    private fun initApi() {
        lifecycleScope.launch {
            diaryViewModel.getDiaryView()
        }
    }

    private fun initEvent() {
        binding.apply {

            // 디데이 설정 안 했을 때 클릭을 위한 뷰
            vDiaryDday.setOnClickListener {
                findNavController().navigate(R.id.action_diaryFragment_to_firstMetDayFragment)
            }

            //일기장 추가
            ivDiaryAdd.setOnClickListener {
                findNavController().navigate(R.id.action_diaryFragment_to_CreateDiary)
            }

            //일기장 보내기 버튼
            tvDiarySend.setOnClickListener {
                diaryViewModel.sendDiaryBooks()
            }

            //일기장 커버
            ivDiaryCover.setOnClickListener {
                Intent(requireContext(), InsideDiaryActivity::class.java).apply {
                    putExtra(InsideDiaryActivity.DIARY_BOOK_ID_KEY, diaryViewModel.diaryBook.value)
                    startActivity(this)
                }
            }

            // 띵동! 벨 울리기
            tvDiaryBell.setOnClickListener {
                diaryViewModel.pushPartner()
                doVibrate(100)
            }
        }
    }

    private fun initObserve() {
        diaryViewModel.apply {

            // 다이어리 뷰 조회 결과
            diaryViewInfo.observe(viewLifecycleOwner) { resource ->
                handleResource(resource, listener = object : ResourceSuccessListener<DiaryViewRes> {
                    override fun onSuccess(res: DiaryViewRes) {
                        handleDiaryViewResult(res)
                    }
                })
            }

            // 일기 보내기 결과
            diaryPassResult.observe(viewLifecycleOwner, EventObserver { resource ->
                handleResource(resource, listener = object : ResourceSuccessListener<SendDiaryBooksRes> {
                    override fun onSuccess(res: SendDiaryBooksRes) {
                        handleDiaryPassResult(res)
                    }
                })
            })

            // 띵동 벨 울리기 결과
            diaryViewModel.pushPartnerResult.observe(viewLifecycleOwner, EventObserver { resources ->
                handleResource(resources, listener = object : ResourceSuccessListener<PushPartnerRes> {
                    override fun onSuccess(res: PushPartnerRes) {
                        handlePushPartnerRes(res)
                    }
                })
            })
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
                    diaryBookStatusId.value = diaryInfo.diaryBookStatus

                    diaryInfo.diaryBook?.let { diary ->
                        diaryBook.value = diary
                        diaryName.value = diary.name
                        diaryCoverNum.value = diary.coverNum
                        diary.diaryBookId
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
    private fun handleDiaryPassResult(res: SendDiaryBooksRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                // 가는 중으로 UI 변경
                diaryViewModel.diaryBookStatusId.value = DiaryStatusType.GOING.id
            }
            2071 -> {
                showNoWritePopup()
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }


    /**
     * 벨 울리기 결과 핸들링
     *
     * @param res
     */
    private fun handlePushPartnerRes(res: PushPartnerRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                if (!res.result.success) {
                    // 벨 울리기 횟수를 모두 소진한 경우
                    showSpendAllBell()
                } else {
                    //Nothing
                }
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }


    /**
     * 벨 울리기 모두 소진한 경우 보여줄 다이얼로그
     *
     */
    private fun showSpendAllBell() {
        DialogPopupOneBtn(getString(R.string.diary_spend_all_bell), getString(R.string.ok)) {
            //Nothing
        }.show(childFragmentManager, DialogPopupOneBtn.TAG)
    }


    /**
     * 일기를 작성하지 않은 경우 보여줄 다이얼로그
     */
    private fun showNoWritePopup() {
        DialogPopupOneBtn(getString(R.string.diary_popup_no_write), getString(R.string.ok)) {
            //Nothing
        }.show(childFragmentManager, DialogPopupOneBtn.TAG)
    }

}