package com.github.syari.ss.plugins.core.time

import java.time.DayOfWeek

data class ScheduleTimeEveryDay(
    val hour: Int, val minute: Int
) {
    val everyHour by lazy { ScheduleTimeEveryHour(minute) }

    companion object {
        fun create(
            hour: Int, minute: Int
        ): ScheduleTimeEveryDay {
            return ScheduleTimeEveryWeek.create(DayOfWeek.MONDAY, hour, minute).everyDay
        }
    }
}