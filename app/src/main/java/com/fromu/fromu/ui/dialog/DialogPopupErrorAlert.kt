package com.fromu.fromu.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.fromu.fromu.databinding.DialogPopupErrorAlertBinding

class DialogPopupErrorAlert(
    private val contents: String,
    private val btnText: String,
    private val listener: () -> Unit
) : DialogFragment() {
    companion object {
        const val TAG = "DialogPopupErrorAlert"
    }

    private var _binding: DialogPopupErrorAlertBinding? = null
    private val binding: DialogPopupErrorAlertBinding
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
        _binding = DialogPopupErrorAlertBinding.inflate(layoutInflater)

        binding.apply {
            contents = this@DialogPopupErrorAlert.contents
            btnText = this@DialogPopupErrorAlert.btnText

            tvPopupOneBtnConfirm.setOnClickListener {
                listener()
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