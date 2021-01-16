@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.time

data class ScheduleTimeEveryHour(
    val minute: Int
) {
    companion object {
        fun create(minute: Int): ScheduleTimeEveryHour {
            return ScheduleTimeEveryDay.create(0, minute).everyHour
        }
    }
}
