package com.fromu.fromu.ui.main.mailbox.maillist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.data.dto.MailListResult
import com.fromu.fromu.data.remote.network.response.MailListRes
import com.fromu.fromu.databinding.FragmentSendMailListBinding
import com.fromu.fromu.model.listener.MailListListener
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.main.mailbox.maillist.adapter.SendMailListRvAdapter
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.MailListViewModel

class SendMailListFragment : BaseFragment<FragmentSendMailListBinding>(FragmentSendMailListBinding::inflate) {

    private val mailListViewModel: MailListViewModel by activityViewModels()
    private lateinit var sendMailListRvAdapter: SendMailListRvAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewCreateCallApi()
        initView()
        initObserve()
    }

    private fun initData() {}

    private fun initView() {
        binding.apply {
            lifecycleOwner = this@SendMailListFragment
            vm = mailListViewModel
        }

        settingSendMailListRv()
    }

    private fun initObserve() {
        mailListViewModel.getSendMailListResult.observe(viewLifecycleOwner) { resources ->
            handleResource(resources, listener = object : ResourceSuccessListener<MailListRes> {
                override fun onSuccess(res: MailListRes) {
                    handleSendMailListResult(res)
                }
            })
        }
    }


    private fun viewCreateCallApi() {
        mailListViewModel.getSendMailList()
    }

    private fun handleSendMailListResult(res: MailListRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                mailListViewModel.isExistSendMail.value = res.result.isNotEmpty()
                sendMailListRvAdapter.submitList(res.result)
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }

    /**
     * 보낸 편지 리스트 보여줄 리사이클러뷰 셋팅
     */
    private fun settingSendMailListRv() {
        sendMailListRvAdapter = SendMailListRvAdapter(object : MailListListener {
            override fun onSelect(letterInfo: MailListResult) {
                val action = MailListFragmentDirections.actionMailListFragmentToLetterDetailFragment(letterInfo.letterId, letterInfo.readFlag, false)
                findNavController().navigate(action)
            }
        })
        binding.rvSendMailList.adapter = sendMailListRvAdapter
    }
}