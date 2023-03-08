package com.fromu.fromu.utils.binder

import android.view.View
import android.widget.ImageView
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
}