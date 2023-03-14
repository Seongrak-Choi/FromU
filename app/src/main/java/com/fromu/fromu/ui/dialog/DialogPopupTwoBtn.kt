package com.fromu.fromu.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.fromu.fromu.databinding.DialogPopupTwoBtnBinding

class DialogPopupTwoBtn(
    private val contents: String,
    private val negativeBtnText: String,
    private val positiveBtnText: String,
    private val listener: DialogPopupTwoBtnListener
) : DialogFragment() {
    companion object {
        const val TAG = "DialogPopupTwoBtn"
    }

    interface DialogPopupTwoBtnListener {
        fun onNegative()
        fun onPositive()
    }

    private var _binding: DialogPopupTwoBtnBinding? = null
    private val binding: DialogPopupTwoBtnBinding
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
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogPopupTwoBtnBinding.inflate(layoutInflater)

        binding.apply {
            contents = this@DialogPopupTwoBtn.contents
            negativeBtnText = this@DialogPopupTwoBtn.negativeBtnText
            positiveBtnText = this@DialogPopupTwoBtn.positiveBtnText


            tvPopupTwoBtnNegative.setOnClickListener {
                listener.onNegative()
                dismiss()
            }

            tvPopupTwoBtnPositive.setOnClickListener {
                listener.onPositive()
                dismiss()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}