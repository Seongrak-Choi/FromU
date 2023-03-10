package com.fromu.fromu.utils.binder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.fromu.fromu.model.FindDiaryCover

object DiaryBinder {
    @JvmStatic
    @BindingAdapter("diaryCoverByCoverId")
    fun setDiaryCoverImgByInt(view: ImageView, coverId: Int) {
        Glide.with(view)
            .load(FindDiaryCover.getShadowCoverDrawableById(coverId))
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("visibleByDiaryStatusId")
    fun setVisibleByDiaryStatusId(view: View, diaryStatusId: String) {
        view.visibility = if (view.tag == diaryStatusId) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("visibleByWeatherId")
    fun setVisibleByWeatherId(view: View, weatherId: String?) {
        view.visibility = if (view.tag == weatherId) View.VISIBLE else View.INVISIBLE
    }

    @JvmStatic
    @BindingAdapter("dayOfWeek")
    fun setDayOfWeekByInt(view: TextView, dayOfWeekId: Int?) {
        val dayOfWeek: String = when (dayOfWeekId) {
            1 -> "월요일"
            2 -> "화요일"
            3 -> "수요일"
            4 -> "목요일"
            5 -> "금요일"
            6 -> "토요일"
            7 -> "일요일"
            else -> ""
        }
        view.text = dayOfWeek
    }
}