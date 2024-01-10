package com.fromu.fromu.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.fromu.fromu.databinding.DialogPopupTextAlertBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DialogPopupTextAlert(
    private val contents: String,
) : DialogFragment() {
    companion object {
        const val TAG = "DialogPopupTextAlert"
    }

    private var _binding: DialogPopupTextAlertBinding? = null
    private val binding: DialogPopupTextAlertBinding
        get() = _binding!!


    override fun onResume() {
        super.onResume()

        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val params = dialog?.window?.attributes
            params?.apply {
                width = (resources.displayMetrics.widthPixels * 0.9).toInt()
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }

            attributes = params

            CoroutineScope(Dispatchers.Main).launch {
                delay(1500) //1.5초 뒤에 다이얼로그 화면 자동으로 닫히도록 설정
                dialog?.dismiss()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogPopupTextAlertBinding.inflate(layoutInflater)

        binding.apply {
            contents = this@DialogPopupTextAlert.contents
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}