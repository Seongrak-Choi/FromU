package com.fromu.fromu.utils.custom

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.fromu.fromu.FromUApplication
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
        initData()
        initView()
    }

    private fun initData() {
        snackBarBinding.message = message
    }

    private fun initView() {
        with(snackBarLayout) {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            gravity = (Gravity.CENTER or Gravity.TOP)
        }

        with(snackBarView) {
            removeAllViews()
            setPadding(0, FromUApplication.statusHeight, 0, 0)

            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(snackBarBinding.root, 0)
        }


    }


    fun show() {
        snackBar.show()
    }
}