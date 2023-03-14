package com.fromu.fromu.utils.binder

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.fromu.fromu.R

object MailBoxBinder {
    @JvmStatic
    @BindingAdapter("srcByNewEmailCount")
    fun isHaveNewMail(view: ImageView, newEmailCount: Int) {
        if (newEmailCount > 0)
            view.setImageResource(R.drawable.ic_mail_box_exist)
        else
            view.setImageResource(R.drawable.ic_mail_box_empty)
    }
}