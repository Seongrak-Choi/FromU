package com.fromu.fromu.ui.main.myhome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.viewModels
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.response.SetBellMsgRes
import com.fromu.fromu.databinding.FragmentAlarmMsgSettingBinding
import com.fromu.fromu.databinding.PopupAlarmMsgMenuBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.main.MainActivity
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.MyHomeViewModel

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

    private fun initData() {}
    private fun initView() {
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).apply {
                isVisibleBottomNav(false)
                isVisibleAppbar(false)
            }
        }
    }

    private fun initEvent() {
        binding.apply {
            clAlarmMsgSelect.setOnClickListener {
                showPopupMenu(it, layoutInflater)
            }
        }
    }

    private fun initObserve() {
        myHomeViewModel.setBellMsgResult.observe(viewLifecycleOwner) { resources ->
            handleResource(resources, true, object : ResourceSuccessListener<SetBellMsgRes> {
                override fun onSuccess(res: SetBellMsgRes) {
                    handelSetBellMsgRes(res)
                }
            })
        }
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
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )


        // popupWindow를 제외한 다른 부분 선택시 메뉴가 꺼지도록 popupWindow에 포커스를 줌
        mPopupWindow.isFocusable = true
        mPopupWindow.animationStyle = R.style.PopupWindowUpDownAnimation

        // 앱바 밑에 출력 되도록 셋팅
        mPopupWindow.showAsDropDown(targetView)
    }

    private fun handelSetBellMsgRes(res: SetBellMsgRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                //TODO 벨 메세지 변경 후 구현
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }
}