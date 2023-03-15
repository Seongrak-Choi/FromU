package com.fromu.fromu.utils.binder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.fromu.fromu.R
import com.fromu.fromu.model.FindStamp
import com.fromu.fromu.utils.Utils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object MailBoxBinder {
    @JvmStatic
    @BindingAdapter("srcByNewEmailCount")
    fun isHaveNewMail(view: ImageView, newEmailCount: Int) {
        if (newEmailCount > 0)
            view.setImageResource(R.drawable.ic_mail_box_exist)
        else
            view.setImageResource(R.drawable.ic_mail_box_empty)
    }


    @JvmStatic
    @BindingAdapter("mailListDate")
    fun setMailListDate(view: TextView, date: String) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(date, formatter)
        val mContext = view.context

        view.text = mContext.getString(R.string.mail_list_date).format(String.format("%02d", dateTime.monthValue), String.format("%02d", dateTime.dayOfMonth))
    }

    @JvmStatic
    @BindingAdapter("letterDate")
    fun setLetterDate(view: TextView, date: String?) {
        val mContext = view.context

        date?.let {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val dateTime = LocalDateTime.parse(it, formatter)
            val amPmValue = dateTime.format(DateTimeFormatter.ofPattern("a"))

            view.text = mContext.getString(R.string.letter_date).format(
                dateTime.year.toString(),
                String.format("%02d", dateTime.monthValue),
                String.format("%02d", dateTime.dayOfMonth),
                amPmValue.uppercase(Locale.ROOT),
                String.format("%02d", dateTime.hour),
                String.format("%02d", dateTime.minute)
            )
        }
    }

    @JvmStatic
    @BindingAdapter("paperBackgroundByStampId")
    fun setPaperBackground(view: View, stampId: Int) {
        view.setBackgroundResource(FindStamp.getPaperDrawableById(stampId))
    }

    @JvmStatic
    @BindingAdapter("stampSize5858Background")
    fun setStampSrc(view: ImageView, stampId: Int) {
        view.setImageResource(FindStamp.getStampSize5858DrawableById(stampId))
    }

    @JvmStatic
    @BindingAdapter("bottomBtnVisible")
    fun setBottomPaddingByBtnVisible(view: View, isBtnVisible: Boolean) {
        if (isBtnVisible) view.setPadding(0, Utils.dp2px(view.resources, 30f), 0, Utils.dp2px(view.resources, 25f))
    }
}