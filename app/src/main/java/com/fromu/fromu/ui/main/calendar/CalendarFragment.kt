package com.fromu.fromu.ui.main.calendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.fromu.fromu.data.remote.network.response.GetSchedulesListRes
import com.fromu.fromu.databinding.FragmentCalendarBinding
import com.fromu.fromu.model.listener.DialogCloseListener
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.dialog.DialogBottomSchedule
import com.fromu.fromu.ui.main.MainActivity
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.utils.calendar.CalendarEventDayDecorator
import com.fromu.fromu.utils.calendar.DayDecorator
import com.fromu.fromu.viewmodels.CalendarViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding>(FragmentCalendarBinding::inflate) {

    private val calendarViewModel: CalendarViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObserve()
        initEvent()
    }

    private fun initData() {}

    private fun initView() {
        activity?.let { activity ->
            if (activity is MainActivity) {
                activity.isVisibleBottomNav(true)
                activity.isVisibleAppbar(true)
            }
        }

        binding.apply {
            lifecycleOwner = this@CalendarFragment
        }

        settingCalendar()
    }

    private fun initObserve() {
        calendarViewModel.apply {
            getSchedulesListResult.observe(viewLifecycleOwner) { resources ->
                handleResource(resources, false, object : ResourceSuccessListener<GetSchedulesListRes> {
                    override fun onSuccess(res: GetSchedulesListRes) {
                        handleSchedulesListRes(res)
                    }
                })
            }
        }
    }

    private fun initEvent() {}

    /**
     *
     *
     */
    private fun settingCalendar() {
        binding.calendar.apply {
            callScheduleList()

            setOnDateChangedListener { widget, date, selected ->
                showDetailScheduleDialog(date)
            }

            setOnMonthChangedListener { widget, date ->
                callScheduleList()
            }
        }
    }


    private fun handleSchedulesListRes(res: GetSchedulesListRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                setDotsAndDecorator(binding.calendar, res.result)
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }

    /**
     * 이벤트가 있는 일(day)에 dot을 찍기 위해 서버에서 받은 day를 CalendarDay형으로 변환하는 메소드
     *
     * @param selectDate
     * @param dayList
     * @return
     */
    private fun convertToCalendarDay(selectDate: CalendarDay, dayList: ArrayList<Int>): List<CalendarDay> {
        return dayList.map { CalendarDay.from(selectDate.year, selectDate.month, it) }
    }


    /**
     * 달력 커스텀에 필요한 decorator와 이벤트가 있는 날에 dot을 찍는 메소드
     *
     * @param calendar
     */
    private fun setDotsAndDecorator(calendar: MaterialCalendarView, dayList: ArrayList<Int>) {
        calendar.apply {
            removeDecorators()
            addDecorators(DayDecorator(requireContext()))
            addDecorator(CalendarEventDayDecorator(convertToCalendarDay(currentDate, dayList)))
        }
    }

    private fun callScheduleList() {
        binding.calendar.apply {
            calendarViewModel.getScheduleList("${currentDate.year}${String.format("%02d", currentDate.month + 1)}")
        }
    }


    /**
     * 선택한 일의 일정을 보여주는 다이얼로그 show
     *
     * @param date
     */
    private fun showDetailScheduleDialog(date: CalendarDay) {
        DialogBottomSchedule(
            "${date.year}${String.format("%02d", date.month + 1)}",
            String.format("%02d", date.day),
            object : DialogCloseListener {
                override fun onClose() {
                    callScheduleList()
                }
            }
        ).show(childFragmentManager, DialogBottomSchedule.TAG)
    }
}