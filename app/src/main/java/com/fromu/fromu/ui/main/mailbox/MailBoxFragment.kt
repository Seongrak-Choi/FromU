package com.fromu.fromu.ui.main.mailbox

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.fromu.fromu.data.remote.network.response.MailBoxViewRes
import com.fromu.fromu.databinding.FragmentMailBoxBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.main.MainActivity
import com.fromu.fromu.ui.main.mailbox.maillist.MailListActivity
import com.fromu.fromu.ui.main.mailbox.stampbox.StampBoxActivity
import com.fromu.fromu.ui.main.mailbox.write.WriteLetterActivity
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.MailBoxViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MailBoxFragment : BaseFragment<FragmentMailBoxBinding>(FragmentMailBoxBinding::inflate) {
    private val mailBoxViewModel: MailBoxViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        callApi()
        initObserve()
        initEvent()
    }

    private fun initData() {}

    private fun callApi() {
        mailBoxViewModel.getMailBoxView()
    }

    private fun initView() {
        activity?.let { activity ->
            if (activity is MainActivity) {
                activity.isVisibleBottomNav(true)
                activity.isVisibleAppbar(true)
            }
        }

        binding.apply {
            lifecycleOwner = this@MailBoxFragment
            vm = mailBoxViewModel
        }
    }

    private fun initObserve() {
        mailBoxViewModel.mailBoxViewResult.observe(viewLifecycleOwner) { resource ->
            handleResource(resource, listener = object : ResourceSuccessListener<MailBoxViewRes> {
                override fun onSuccess(res: MailBoxViewRes) {
                    handleMailBoxRes(res)
                }
            })
        }
    }

    private fun initEvent() {
        binding.apply {
            // 우표함 버튼
            ivMailBoxStamp.setOnClickListener {
                Intent(requireContext(), StampBoxActivity::class.java).apply {
                    startActivity(this)
                }
            }

            // 편지 쓰기 버튼
            ivMailBoxWrite.setOnClickListener {
                Intent(requireContext(), WriteLetterActivity::class.java).apply {
                    startActivity(this)
                }
            }

            // 우편함 버튼
            clMailBox.setOnClickListener {
                Intent(requireContext(), MailListActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
    }

    /**
     * 우편함 뷰 조회 핸들링
     *
     * @param res
     */
    private fun handleMailBoxRes(res: MailBoxViewRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                mailBoxViewModel.mailBoxName.value = res.result.mailboxName
                mailBoxViewModel.newLetterId.value = res.result.newLetterId
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }
}