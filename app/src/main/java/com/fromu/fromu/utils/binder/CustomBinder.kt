package com.fromu.fromu.utils.binder

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat

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

    @JvmStatic
    @BindingAdapter("imgResource")
    fun setImgResourceByGlide(view: ImageView, imgResource: Int) {
        view.setImageResource(imgResource)
    }

    @JvmStatic
    @BindingAdapter("imgResource")
    fun setImgResourceByGlide(view: ImageView, imgResource: String?) {
        Glide.with(view)
            .load(imgResource)
            .format(DecodeFormat.PREFER_RGB_565)
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("playLottie")
    fun setLottieAutoPlay(view: LottieAnimationView, isPlay: Boolean) {
        if (isPlay) view.playAnimation()
    }
}