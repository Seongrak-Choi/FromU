package com.fromu.fromu.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.fromu.fromu.databinding.DialogPopupOneBtnBinding
import com.fromu.fromu.model.listener.DialogPopupOneBtnListener

class DialogPopupOneBtn(private val contents: String, private val btnText: String, private val listener: DialogPopupOneBtnListener) : DialogFragment() {
    companion object {
        const val TAG = "DialogPopupOneBtn"
    }

    private var _binding: DialogPopupOneBtnBinding? = null
    private val binding: DialogPopupOneBtnBinding
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
        _binding = DialogPopupOneBtnBinding.inflate(layoutInflater)

        binding.apply {
            contents = this@DialogPopupOneBtn.contents
            btnText = this@DialogPopupOneBtn.btnText

            tvPopupOneBtnConfirm.setOnClickListener {
                listener.onResult()
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