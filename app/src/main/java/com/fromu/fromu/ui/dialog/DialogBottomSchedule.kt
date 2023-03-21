package com.fromu.fromu.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.fromu.fromu.R
import com.fromu.fromu.data.dto.DetailScheduleResult
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.AddSchedulesReq
import com.fromu.fromu.data.remote.network.request.PatchSchedulesReq
import com.fromu.fromu.data.remote.network.response.AddSchedulesRes
import com.fromu.fromu.data.remote.network.response.DeleteSchedulesRes
import com.fromu.fromu.data.remote.network.response.GetDetailScheduleRes
import com.fromu.fromu.data.remote.network.response.PatchSchedulesRes
import com.fromu.fromu.databinding.DialogDetailScheduleBinding
import com.fromu.fromu.model.listener.DialogCloseListener
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.main.calendar.DetailScheduleRvAdapter
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.DialogDetailScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogBottomSchedule(private val month: String, private val day: String, private val listener: DialogCloseListener) : DialogFragment() {

    companion object {
        const val TAG = "DialogBottomSchedule"
    }

    enum class DetailScheduleUiType {
        INDEX, // 일정 목록 화면
        INPUT // 일정 수정 or 추가 화면
    }

    enum class DetailScheduleInputType {
        MODIFY, //수정
        ADD, // 추가
    }

    private val dialogDetailScheduleViewModel: DialogDetailScheduleViewModel by viewModels()
    private var binding: DialogDetailScheduleBinding? = null
    private lateinit var detailScheduleRvAdapter: DetailScheduleRvAdapter

    // 수정할 일정의 아이디를 저장
    private var modifyScheduleId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callDetailSchedule()
        initData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogDetailScheduleBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObserve()
        initEvent()
    }

    override fun onResume() {
        super.onResume()
        setDialogWindow()
    }

    private fun initData() {}
    private fun initView() {
        binding?.apply {
            lifecycleOwner = this@DialogBottomSchedule
            vm = dialogDetailScheduleViewModel
        }
        settingDetailScheduleRv()
    }

    private fun initObserve() {
        dialogDetailScheduleViewModel.apply {

            getDetailScheduleResult.observe(viewLifecycleOwner) { resources ->
                handleResource(resources, true, object : ResourceSuccessListener<GetDetailScheduleRes> {
                    override fun onSuccess(res: GetDetailScheduleRes) {
                        handleGetDetailScheduleRes(res)
                    }
                })
            }


            addScheduleResult.observe(viewLifecycleOwner) { resources ->
                handleResource(resources, true, object : ResourceSuccessListener<AddSchedulesRes> {
                    override fun onSuccess(res: AddSchedulesRes) {
                        handleAddScheduleRes(res)
                    }
                })
            }

            deleteScheduleResult.observe(viewLifecycleOwner) { resources ->
                handleResource(resources, true, object : ResourceSuccessListener<DeleteSchedulesRes> {
                    override fun onSuccess(res: DeleteSchedulesRes) {
                        handleDeleteScheduleRes(res)
                    }
                })
            }

            patchScheduleResult.observe(viewLifecycleOwner) { resources ->
                handleResource(resources, true, object : ResourceSuccessListener<PatchSchedulesRes> {
                    override fun onSuccess(res: PatchSchedulesRes) {
                        handlePatchScheduleRes(res)
                    }
                })

            }
        }
    }

    private fun initEvent() {
        binding?.apply {

            // 일정 추가 하기 버튼
            llDetailScheduleNoItemAdd.setOnClickListener {
                changeUiType(DetailScheduleInputType.ADD)
            }

            // 이전 버튼
            tvDetailScheduleBack.setOnClickListener {
                dialogDetailScheduleViewModel.uiType.value = DetailScheduleUiType.INDEX
            }

            // Ui가 InputType일 때 수정 or 완료 버튼
            tvDetailScheduleDone.setOnClickListener {
                hideKeyboard()

                dialogDetailScheduleViewModel.apply {
                    when (inputType.value) {
                        DetailScheduleInputType.ADD -> {
                            // 등록 api 호출
                            dialogDetailScheduleViewModel.addSchedules(AddSchedulesReq(scheduleInputData.value, month + day))
                        }
                        DetailScheduleInputType.MODIFY -> {
                            // 수정 api 호출
                            dialogDetailScheduleViewModel.patchSchedules(modifyScheduleId, PatchSchedulesReq(scheduleInputData.value, month + day))
                        }
                    }
                }
            }

            // 일정 입력창
            etDetailSchedule.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                vDetailScheduleInputUnderline.isSelected = hasFocus
            }
        }
    }


    /**
     * 다이얼로그 window 셋팅
     *
     */
    private fun setDialogWindow() {
        dialog?.window?.apply {
            setGravity(Gravity.BOTTOM)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val params = attributes
            params?.apply {
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = (resources.displayMetrics.heightPixels * 0.4).toInt()
            }

            attributes = params
        }
    }


    /**
     * 리사이클러뷰 셋팅
     */
    private fun settingDetailScheduleRv() {
        binding?.apply {
            detailScheduleRvAdapter = DetailScheduleRvAdapter(object : DetailScheduleRvAdapter.DetailScheduleRvListener {
                override fun onClickDelete(scheduleId: Int, position: Int) {
                    showCheckDeleteScheduleDialog(scheduleId, position)
                }

                override fun onClickModify(content: String, scheduleId: Int) {
                    modifyScheduleId = scheduleId
                    dialogDetailScheduleViewModel.scheduleInputData.value = content
                    changeUiType(DetailScheduleInputType.MODIFY)
                }

                override fun onAddSchedule() {
                    changeUiType(DetailScheduleInputType.ADD)
                }
            })

            rvDetailSchedule.adapter = detailScheduleRvAdapter
        }
    }

    /**
     * 일정 목록 or 입력으로 변경
     */
    private fun changeUiType(inputType: DetailScheduleInputType) {
        dialogDetailScheduleViewModel.apply {
            if (inputType == DetailScheduleInputType.ADD)
                scheduleInputData.value = ""

            this.inputType.value = inputType
            uiType.value = DetailScheduleUiType.INPUT
        }
    }

    /**
     * 해당 일의 일정 조회 api 호출
     *
     */
    private fun callDetailSchedule() {
        dialogDetailScheduleViewModel.getDetailSchedule(month, day)
    }


    /**
     * 일정 삭제 재확인 다이얼로그 호출
     *
     * @param scheduleId
     * @param position
     */
    private fun showCheckDeleteScheduleDialog(scheduleId: Int, position: Int) {
        DialogPopupTwoBtn(getString(R.string.delete_confirm_mention), getString(R.string.back), getString(R.string.delete), object : DialogPopupTwoBtn.DialogPopupTwoBtnListener {
            override fun onNegative() {
                //Nothing
            }

            override fun onPositive() {
                dialogDetailScheduleViewModel.deleteSchedule(scheduleId)
            }
        }).show(childFragmentManager, DialogPopupTwoBtn.TAG)

    }

    /**
     * Resource 핸들링
     *
     * @param T : API Response
     * @param resource
     * @param listener
     */
    open fun <T> handleResource(resource: Resource<T>, isShowLoadingDialog: Boolean = false, listener: ResourceSuccessListener<T>) {
        when (resource) {
            is Resource.Loading -> {
                if (isShowLoadingDialog)
                    dialogDetailScheduleViewModel.isShowDialogLoading.value = true
            }
            is Resource.Success -> {
                dialogDetailScheduleViewModel.isShowDialogLoading.value = false
                listener.onSuccess(resource.body)
            }
            is Resource.Failed -> {
                dialogDetailScheduleViewModel.isShowDialogLoading.value = false

                binding?.apply {
                    Utils.showNetworkErrorSnackBar(root)
                }
            }
        }
    }


    /**
     *
     * 특정 일의 일정 조회 결과 핸들링
     *
     * @param res
     */
    private fun handleGetDetailScheduleRes(res: GetDetailScheduleRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                dialogDetailScheduleViewModel.isExistSchedule.value = res.result.isNotEmpty()
                res.result.add(DetailScheduleResult("", "", false, "", 0, 0)) //마지막 원소를 일정 추가하기 버튼으로 만들기 위해 아이템 추가
                detailScheduleRvAdapter.submitList(res.result)
            }
            else -> {
                binding?.let {
                    Utils.showNetworkErrorSnackBar(it.root)
                }
            }
        }
    }

    /**
     *
     * 일정 등록 결과 핸들링
     *
     * @param res
     */
    private fun handleAddScheduleRes(res: AddSchedulesRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                dialogDetailScheduleViewModel.uiType.value = DetailScheduleUiType.INDEX
                callDetailSchedule()
            }
            else -> {
                binding?.let {
                    Utils.showNetworkErrorSnackBar(it.root)
                }
            }
        }
    }

    /**
     * 일정 삭제 결과 핸들링
     *
     * @param res
     */
    private fun handleDeleteScheduleRes(res: DeleteSchedulesRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                callDetailSchedule()
            }
            else -> {
                binding?.apply {
                    Utils.showNetworkErrorSnackBar(root)
                }
            }
        }
    }


    /**
     * 일정 수정 결과 핸들링
     *
     * @param res
     */
    private fun handlePatchScheduleRes(res: PatchSchedulesRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                dialogDetailScheduleViewModel.uiType.value = DetailScheduleUiType.INDEX
                callDetailSchedule()
            }
            else -> {
                binding?.apply {
                    Utils.showNetworkErrorSnackBar(root)
                }
            }
        }
    }

    /**
     * 키보드를 강제로 내리기 위한 메소드
     */
    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        listener.onClose()
    }
}