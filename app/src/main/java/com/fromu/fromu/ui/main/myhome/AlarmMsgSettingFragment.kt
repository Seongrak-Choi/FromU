package com.fromu.fromu.ui.main.myhome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.request.SetBellMsgReq
import com.fromu.fromu.data.remote.network.response.GetBellMsgRes
import com.fromu.fromu.data.remote.network.response.SetBellMsgRes
import com.fromu.fromu.databinding.FragmentAlarmMsgSettingBinding
import com.fromu.fromu.databinding.PopupAlarmMsgMenuBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.main.MainActivity
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.MyHomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmMsgSettingFragment : BaseFragment<FragmentAlarmMsgSettingBinding>(FragmentAlarmMsgSettingBinding::inflate) {
    private val myHomeViewModel: MyHomeViewModel by viewModels()

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
        callApi()
    }

    private fun initView() {
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).apply {
                isVisibleBottomNav(false)
                isVisibleAppbar(false)
            }
        }

        binding.apply {
            lifecycleOwner = this@AlarmMsgSettingFragment
            vm = myHomeViewModel
        }
    }

    private fun initEvent() {
        binding.apply {
            ivAlarmMsgDropMenu.setOnClickListener {
                hideKeyboard()
                showPopupMenu(it, layoutInflater)
            }

            ivAlarmMsgBack.setOnClickListener {
                findNavController().popBackStack()
            }

            tvAlarmMsgSettingDone.setOnClickListener {
                myHomeViewModel.setBellMsg(SetBellMsgReq(etAlarmMsgSetting.text.toString()))
            }

            etAlarmMsgSetting.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus)
                    myHomeViewModel.currentBellMsg.value = ""
            }
        }
    }

    private fun initObserve() {
        myHomeViewModel.setBellMsgResult.observe(viewLifecycleOwner) { resources ->
            handleResource(resources, true, object : ResourceSuccessListener<SetBellMsgRes> {
                override fun onSuccess(res: SetBellMsgRes) {
                    handleSetBellMsgRes(res)
                }
            })
        }

        myHomeViewModel.getBellMsgResult.observe(viewLifecycleOwner) { resources ->
            handleResource(resources, true, object : ResourceSuccessListener<GetBellMsgRes> {
                override fun onSuccess(res: GetBellMsgRes) {
                    handleGetBellMsgRes(res)
                }
            })
        }
    }

    private fun callApi() {
        myHomeViewModel.getBellMsg()
    }

    /**
     * show 알림 메세지 팝업
     */
    private fun showPopupMenu(
        targetView: View,
        layoutInflater: LayoutInflater
    ) {
        val popupView = PopupAlarmMsgMenuBinding.inflate(layoutInflater)
        val mPopupWindow = PopupWindow(
            popupView.root,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        popupView.apply {
            tvAlarmMsgMenu1.setOnClickListener {
                binding.etAlarmMsgSetting.apply {
                    setText(tvAlarmMsgMenu1.text.toString())
                    clearFocus()
                }

                mPopupWindow.dismiss()
            }

            tvAlarmMsgMenu2.setOnClickListener {
                binding.etAlarmMsgSetting.apply {
                    setText(tvAlarmMsgMenu2.text.toString())
                    clearFocus()
                }
                mPopupWindow.dismiss()
            }

            tvAlarmMsgMenu3.setOnClickListener {
                binding.etAlarmMsgSetting.apply {
                    setText(tvAlarmMsgMenu3.text.toString())
                    clearFocus()
                }
                mPopupWindow.dismiss()
            }

            tvAlarmMsgMenuDirect.setOnClickListener {
                binding.etAlarmMsgSetting.apply {
                    requestFocus()
                    openKeyboard(this)
                }
                mPopupWindow.dismiss()
            }
        }

        // popupWindow를 제외한 다른 부분 선택시 메뉴가 꺼지도록 popupWindow에 포커스를 줌
        mPopupWindow.isFocusable = true
        mPopupWindow.animationStyle = R.style.PopupWindowUpDownAnimation

        // 앱바 밑에 출력 되도록 셋팅
        mPopupWindow.showAsDropDown(targetView)
    }

    /**
     * 벨 울리기 메시지 설정 결과 핸들링
     *
     * @param res
     */
    private fun handleSetBellMsgRes(res: SetBellMsgRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                Utils.showCustomSnackBar(binding.root, "알림 메세지가 변경됐어!")
                findNavController().popBackStack()
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }

    /**
     * 현재 설정 된 벨 멘트 조회 결과 핸들링
     *
     * @param res
     */
    private fun handleGetBellMsgRes(res: GetBellMsgRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                myHomeViewModel.currentBellMsg.value = res.result.pushMessage
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }
}