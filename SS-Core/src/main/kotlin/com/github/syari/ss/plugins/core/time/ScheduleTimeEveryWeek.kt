@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.time

import java.time.DayOfWeek

data class ScheduleTimeEveryWeek(
    val dayOfWeek: DayOfWeek,
    val hour: Int,
    val minute: Int
) {
    fun getNextMinute(): ScheduleTimeEveryWeek {
        return create(dayOfWeek, hour, minute + 1)
    }

    val everyDay by lazy { ScheduleTimeEveryDay(hour, minute) }

    companion object {
        fun create(
            dayOfWeek: DayOfWeek,
            hour: Int,
            minute: Int
        ): ScheduleTimeEveryWeek {
            var nextHour = hour
            val nextMinute = if (60 <= minute) {
                nextHour += (minute / 60)
                minute % 60
            } else {
                minute
            }
            val nextWeek = if (24 <= nextHour) {
                nextHour %= 24
                dayOfWeek.plus(nextHour / 24L)
            } else {
                dayOfWeek
            }
            return ScheduleTimeEveryWeek(nextWeek, nextHour, nextMinute)
        }
    }
}
