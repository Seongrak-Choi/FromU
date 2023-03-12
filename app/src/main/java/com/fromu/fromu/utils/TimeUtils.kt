package com.fromu.fromu.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class TimeUtils {
    companion object {
        private val calendar: Calendar = Calendar.getInstance()

        /**
         * 오늘의 요일 구하는 메소드
         *
         * @return
         */
        fun getToday(): Int {
            return calendar.get(Calendar.DAY_OF_MONTH);
        }

        /**
         * 오늘의 월 구하는 메소드
         *
         * @return
         */
        fun getMonthOfToday(): Int {
            return calendar[Calendar.MONTH] + 1
        }

        /**
         * 오늘의 요일 구하는 메소드
         *
         * @return (ex. 월요일, 화요일, 수요일 ....)
         */
        fun getDayOfWeek(): String {
            return when (calendar[Calendar.DAY_OF_WEEK]) {
                Calendar.SUNDAY -> "일요일"
                Calendar.MONDAY -> "월요일"
                Calendar.TUESDAY -> "화요일"
                Calendar.WEDNESDAY -> "수요일"
                Calendar.THURSDAY -> "목요일"
                Calendar.FRIDAY -> "금요일"
                Calendar.SATURDAY -> "토요일"
                else -> "error"
            }
        }

        /**
         * yyyyMMdd에서 오늘의 일과 월을 추출하는 메소드
         *
         * @param dateString
         * @return Pair<Int, Int>  = pair(월, 일)
         */
        fun getDayAndMonthByYyyyMMdd(dateString: String?): Pair<Int, Int> {
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
            val date = LocalDate.parse(dateString, formatter)
            val month = date.monthValue
            val day = date.dayOfMonth

            return Pair(month, day)
        }


        @JvmStatic
        fun getYearByYyyyMMdd(dateString: String?): Int {
            val year: Int = if (!dateString.isNullOrEmpty() && dateString.toString().length == 8) {
                val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                val date = LocalDate.parse(dateString, formatter)
                date.year
            } else {
                0
            }

            return year
        }

        /**
         * yyyyMMdd에서 월(month)을 추출하는 메소드
         *
         * @param dateString
         * @return Int = 월
         */
        @JvmStatic
        fun getMonthByYyyyMMdd(dateString: String?): Int {
            val month: Int = if (!dateString.isNullOrEmpty() && dateString.toString().length == 8) {
                val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                val date = LocalDate.parse(dateString, formatter)
                date.monthValue
            } else {
                0
            }

            return month
        }

        /**
         * yyyyMMdd에서 일(day)을 추출하는 메소드
         *
         * @param dateString
         * @return Int = 일
         */
        @JvmStatic
        fun getDayByYyyyMMdd(dateString: String?): Int {
            val day: Int = if (!dateString.isNullOrEmpty() && dateString.toString().length == 8) {
                val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                val date = LocalDate.parse(dateString, formatter)
                date.dayOfMonth
            } else {
                0
            }

            return day
        }
    }
}