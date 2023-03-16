package com.fromu.fromu.ui.main.mailbox.maillist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.data.dto.MailListResult
import com.fromu.fromu.data.remote.network.response.MailListRes
import com.fromu.fromu.databinding.FragmentReceiveMailListBinding
import com.fromu.fromu.model.listener.MailListListener
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.main.mailbox.maillist.adapter.ReceiveMailListRvAdapter
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.MailListViewModel

class ReceiveMailListFragment : BaseFragment<FragmentReceiveMailListBinding>(FragmentReceiveMailListBinding::inflate) {

    private val mailListViewModel: MailListViewModel by activityViewModels()
    private lateinit var receiveMailListRvAdapter: ReceiveMailListRvAdapter

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
            lifecycleOwner = this@ReceiveMailListFragment
            vm = mailListViewModel
        }
        settingSendMailListRv()
    }


    private fun viewCreateCallApi() {
        mailListViewModel.getReceiveMailList()
    }

    private fun initObserve() {
        mailListViewModel.apply {
            getReceiveMailListResult.observe(viewLifecycleOwner) { resources ->
                handleResource(resources, listener = object : ResourceSuccessListener<MailListRes> {
                    override fun onSuccess(res: MailListRes) {
                        handleReceiveMailListResult(res)
                    }
                })
            }
        }
    }

    /**
     * 받은 편지 리스트 결과 핸들링
     *
     * @param res
     */
    private fun handleReceiveMailListResult(res: MailListRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                mailListViewModel.isExistReceiveMail.value = res.result.isNotEmpty()
                receiveMailListRvAdapter.submitList(res.result)
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
        receiveMailListRvAdapter = ReceiveMailListRvAdapter(object : MailListListener {
            override fun onSelect(letterInfo: MailListResult) {
                val action = MailListFragmentDirections.actionMailListFragmentToLetterDetailFragment(letterInfo.letterId, letterInfo.readFlag, true)
                findNavController().navigate(action)
            }
        })
        binding.rvReceiveMailList.adapter = receiveMailListRvAdapter
    }
}