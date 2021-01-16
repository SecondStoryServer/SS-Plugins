@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.time.event

import com.github.syari.ss.plugins.core.event.CustomEvent
import com.github.syari.ss.plugins.core.time.ScheduleTimeEveryWeek
import com.github.syari.ss.plugins.core.time.TimeScheduler.getFormatTime
import java.time.DayOfWeek

/**
 * 分が変わった時(XX:XX)に発生するイベントです
 */
open class NextMinuteEvent(
    val dayOfWeek: DayOfWeek,
    val hour: Int,
    val minute: Int
) : CustomEvent() {
    val scheduleTime by lazy { ScheduleTimeEveryWeek(dayOfWeek, hour, minute) }

    val formatTime by lazy { getFormatTime(hour, minute) }
}
