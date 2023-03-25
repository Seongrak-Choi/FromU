package com.fromu.fromu.ui.main.mailbox.write

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.fromu.fromu.R
import com.fromu.fromu.databinding.FragmentWriteStartLottieBinding
import com.fromu.fromu.ui.base.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WriteStartLottieFragment : BaseFragment<FragmentWriteStartLottieBinding>(FragmentWriteStartLottieBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initData() {
        goSelectStampAfterDelay1500()
    }

    private fun initView() {}

    private fun goSelectStampAfterDelay1500() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            findNavController().navigate(R.id.action_writeStartLottie_to_selectStampFragment)
        }
    }
}