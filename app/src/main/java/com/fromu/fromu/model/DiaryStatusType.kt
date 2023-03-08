package com.fromu.fromu.model

/**
 * 일기장이 생성되지 않았으면 0
 * 일기장이 나에게 있으면 1
 * 일기장이 오는 중이면 2
 * 일기장이 가는 중이면 3
 * 일기장이 상대한테 있으면 4
 *
 * @property id
 */
enum class DiaryStatusType(val id: Int) {
    NO_CREATE(0), I_HAVE(1), COMING(2), GOING(3), PARTNER_HAVE(4)
}