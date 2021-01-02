package com.github.syari.ss.plugins.core.time

import com.github.syari.ss.plugins.core.Main.Companion.plugin
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runLater
import com.github.syari.ss.plugins.core.time.event.NextDayEvent
import com.github.syari.ss.plugins.core.time.event.NextHourEvent
import com.github.syari.ss.plugins.core.time.event.NextMinuteEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.time.DayOfWeek
import java.time.LocalDateTime

object TimeScheduler: OnEnable, Listener {
    private val everyWeekScheduler = mutableMapOf<ScheduleTimeEveryWeek, MutableSet<() -> Unit>>()
    private val everyDayScheduler = mutableMapOf<ScheduleTimeEveryDay, MutableSet<() -> Unit>>()
    private val everyHourScheduler = mutableMapOf<ScheduleTimeEveryHour, MutableSet<() -> Unit>>()

    /**
     * 毎週決まった時間に実行されます
     * @param dayOfWeek 曜日
     * @param hour 時間
     * @param minute 分
     * @param run その時間に実行する処理
     */
    fun scheduleEveryWeekAt(
        dayOfWeek: DayOfWeek, hour: Int, minute: Int, run: () -> Unit
    ) {
        everyWeekScheduler.getOrPut(ScheduleTimeEveryWeek.create(dayOfWeek, hour, minute)) { mutableSetOf() }.add(run)
    }

    /**
     * 毎日決まった時間に実行されます
     * @param hour 時間
     * @param minute 分
     * @param run その時間に実行する処理
     */
    fun scheduleEveryDayAt(
        hour: Int, minute: Int, run: () -> Unit
    ) {
        everyDayScheduler.getOrPut(ScheduleTimeEveryDay.create(hour, minute)) { mutableSetOf() }.add(run)
    }

    /**
     * 毎時決まった時間に実行されます
     * @param minute 分
     * @param run その時間に実行する処理
     */
    fun scheduleEveryHourAt(
        minute: Int, run: () -> Unit
    ) {
        everyHourScheduler.getOrPut(ScheduleTimeEveryHour.create(minute)) { mutableSetOf() }.add(run)
    }

    /**
     * XX:XXというフォーマットの時間を取得します
     * @param hour 時間
     * @param minute 分
     * @return [String]
     */
    fun getFormatTime(
        hour: Int, minute: Int
    ): String {
        return "${String.format("%2d", hour)}:${String.format("%2d", minute)}"
    }

    /**
     * プラグインが有効になった時に現在時間を更新します
     */
    override fun onEnable() {
        val now = LocalDateTime.now()
        plugin.runLater(60 - now.second.toLong()) {
            nextMinute(ScheduleTimeEveryWeek.create(now.dayOfWeek, now.hour, now.minute + 1))
        }
    }

    private fun nextMinute(time: ScheduleTimeEveryWeek) {
        if (time.minute == 0) {
            if (time.hour == 0) {
                NextDayEvent(time.dayOfWeek)
            } else {
                NextHourEvent(time.dayOfWeek, time.hour)
            }
        } else {
            NextMinuteEvent(time.dayOfWeek, time.hour, time.minute)
        }.callEvent()
        plugin.runLater(60 * 20) {
            nextMinute(time.getNextMinute())
        }
    }

    @EventHandler
    fun onNextMinute(e: NextMinuteEvent) {
        val everyWeek = e.scheduleTime
        everyWeekScheduler[everyWeek]?.forEach { it.invoke() }
        val everyDay = everyWeek.everyDay
        everyDayScheduler[everyDay]?.forEach { it.invoke() }
        val everyHour = everyDay.everyHour
        everyHourScheduler[everyHour]?.forEach { it.invoke() }
    }
}