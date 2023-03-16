package com.fromu.fromu.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.fromu.fromu.databinding.DialogStampPurchaseBinding

class DialogPopupStampPurchase(
    private val stampId: Int,
    private val listener: DialogPopupStampPurchaseListener
) : DialogFragment() {
    companion object {
        const val TAG = "DialogPopupStampPurchase"
    }

    interface DialogPopupStampPurchaseListener {
        fun onNegative()
        fun onPositive()
    }

    private var _binding: DialogStampPurchaseBinding? = null
    private val binding: DialogStampPurchaseBinding
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
        _binding = DialogStampPurchaseBinding.inflate(layoutInflater)

        binding.apply {
            stampId = this@DialogPopupStampPurchase.stampId


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