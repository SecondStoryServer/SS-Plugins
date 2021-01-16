@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.time

import com.github.syari.ss.plugins.core.Main.Companion.plugin
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runLater
import com.github.syari.ss.plugins.core.time.event.NextDayEvent
import com.github.syari.ss.plugins.core.time.event.NextHourEvent
import com.github.syari.ss.plugins.core.time.event.NextMinuteEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.time.DayOfWeek
import java.time.LocalDateTime

object TimeScheduler : OnEnable, Listener {
    class TimeSchedules {
        val everyWeekScheduler = mutableMapOf<ScheduleTimeEveryWeek, MutableSet<() -> Unit>>()
        val everyDayScheduler = mutableMapOf<ScheduleTimeEveryDay, MutableSet<() -> Unit>>()
        val everyHourScheduler = mutableMapOf<ScheduleTimeEveryHour, MutableSet<() -> Unit>>()
    }

    private val schedules = mutableMapOf<String, TimeSchedules>()

    private fun JavaPlugin.schedules() = schedules.getOrPut(name, ::TimeSchedules)

    /**
     * 毎週決まった時間に実行されます
     * @param dayOfWeek 曜日
     * @param hour 時間
     * @param minute 分
     * @param action その時間に実行する処理
     */
    fun JavaPlugin.scheduleEveryWeekAt(
        dayOfWeek: DayOfWeek,
        hour: Int,
        minute: Int,
        action: () -> Unit
    ) {
        schedules().everyWeekScheduler.getOrPut(ScheduleTimeEveryWeek.create(dayOfWeek, hour, minute), ::mutableSetOf).add(action)
    }

    /**
     * 毎日決まった時間に実行されます
     * @param hour 時間
     * @param minute 分
     * @param action その時間に実行する処理
     */
    fun JavaPlugin.scheduleEveryDayAt(
        hour: Int,
        minute: Int,
        action: () -> Unit
    ) {
        schedules().everyDayScheduler.getOrPut(ScheduleTimeEveryDay.create(hour, minute), ::mutableSetOf).add(action)
    }

    /**
     * 毎時決まった時間に実行されます
     * @param minute 分
     * @param action その時間に実行する処理
     */
    fun JavaPlugin.scheduleEveryHourAt(
        minute: Int,
        action: () -> Unit
    ) {
        schedules().everyHourScheduler.getOrPut(ScheduleTimeEveryHour.create(minute), ::mutableSetOf).add(action)
    }

    /**
     * 特定のプラグインのスケジュールを削除します
     */
    fun JavaPlugin.clearTimeScheduler() {
        schedules.remove(name)
    }

    /**
     * XX:XXというフォーマットの時間を取得します
     * @param hour 時間
     * @param minute 分
     * @return [String]
     */
    fun getFormatTime(
        hour: Int,
        minute: Int
    ): String {
        return "${String.format("%02d", hour)}:${String.format("%02d", minute)}"
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
        schedules.values.forEach { list ->
            val everyWeek = e.scheduleTime
            list.everyWeekScheduler[everyWeek]?.forEach { it() }
            val everyDay = everyWeek.everyDay
            list.everyDayScheduler[everyDay]?.forEach { it() }
            val everyHour = everyDay.everyHour
            list.everyHourScheduler[everyHour]?.forEach { it() }
        }
    }
}
