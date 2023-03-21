package com.fromu.fromu.utils.calendar

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import com.fromu.fromu.R
import com.fromu.fromu.utils.Logger
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class DayDecorator(private val mContext: Context) : DayViewDecorator {

    private val drawable = AppCompatResources.getDrawable(mContext, R.drawable.calendar_selector)

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade) {
        drawable?.let {
            view.setSelectionDrawable(it)
        } ?: let {
            Logger.e("DayDecorator", "drawable is null")
        }
    }

}