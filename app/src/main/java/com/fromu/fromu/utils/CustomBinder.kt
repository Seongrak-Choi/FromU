package com.fromu.fromu.utils

import android.view.View
import androidx.databinding.BindingAdapter

object CustomBinder {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun setVisibility(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("isInvisible")
    fun setInVisibility(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }
}