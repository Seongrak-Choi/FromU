package com.fromu.fromu.utils.custom

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.fromu.fromu.databinding.CustomSnackbarBlackBinding
import com.google.android.material.snackbar.Snackbar

class FromUSnackBarBlack(view: View, private val message: String, private val duration: Int) {
    companion object {

        fun make(view: View, message: String, duration: Int) = FromUSnackBarBlack(view, message, duration)
    }

    private val context = view.context
    private val snackBar = Snackbar.make(view, "", duration)
    private val snackBarView = snackBar.view as Snackbar.SnackbarLayout
    private val snackBarLayout = snackBarView.layoutParams as FrameLayout.LayoutParams

    private val inflater = LayoutInflater.from(context)
    private val snackBarBinding: CustomSnackbarBlackBinding = CustomSnackbarBlackBinding.inflate(inflater)

    init {
        initView()
        initData()
    }

    private fun initView() {
        with(snackBarLayout) {
            width = ViewGroup.LayoutParams.WRAP_CONTENT
            gravity = (Gravity.CENTER or Gravity.BOTTOM)
        }

        with(snackBarView) {
            removeAllViews()
            setPadding(0, 0, 0, 30)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(snackBarBinding.root, 0)
        }
    }

    private fun initData() {
        snackBarBinding.message = message
    }

    fun show() {
        snackBar.show()
    }
}