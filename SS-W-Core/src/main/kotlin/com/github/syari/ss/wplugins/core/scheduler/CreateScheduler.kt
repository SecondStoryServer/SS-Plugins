package com.github.syari.ss.wplugins.core.scheduler

import com.github.syari.ss.wplugins.core.Main.Companion.plugin
import net.md_5.bungee.api.plugin.Plugin

object CreateScheduler {
    /**
     * @param action 実行する処理
     * @return [CustomRunnable]
     */
    fun Plugin.schedule(
        action: CustomTask.() -> Unit
    ): CustomRunnable {
        return CustomRunnable(this, action)
    }

    /**
     * @param action 実行する処理
     * @return [CustomTask]?
     */
    fun Plugin.runSchedule(
        action: CustomTask.() -> Unit
    ): CustomTask? {
        return schedule(action).runSchedule()
    }

    /**
     * @param delay 遅らせる時間 tick
     * @param action 遅らせて実行する処理
     * @return [CustomTask]?
     */
    fun Plugin.runLater(
        delay: Long,
        action: CustomTask.() -> Unit
    ): CustomTask? {
        return schedule(action).runLater(delay)
    }

    /**
     * @param period 繰り返し間隔 tick
     * @param delay 遅らせる時間 tick default: 0
     * @param action 繰り返し実行する処理
     * @return [CustomTask]?
     */
    fun Plugin.runTimer(
        period: Long,
        delay: Long = 0,
        action: CustomTask.() -> Unit
    ): CustomTask? {
        return schedule(action).runTimer(period, delay)
    }

    /**
     * @param period 繰り返し間隔 tick
     * @param times 繰り返し回数
     * @param delay 遅らせる時間 tick default: 0
     * @param action 繰り返し実行する処理
     * @return [CustomTask]?
     */
    fun Plugin.runRepeatTimes(
        period: Long,
        times: Int,
        delay: Long = 0,
        action: CustomTask.() -> Unit
    ): CustomTask? {
        return schedule(action).runRepeatTimes(period, times, delay)
    }

    /**
     * @param listWithDelay キーを待機時間としたマップ
     * @param action 待機後に実行する処理
     * @return [Set]<[CustomTask]>
     */
    fun <T> runListWithDelay(
        listWithDelay: Map<Long, Set<T>>,
        action: (T) -> Unit
    ): Set<CustomTask> {
        return mutableSetOf<CustomTask>().also { taskList ->
            listWithDelay.forEach { (delay, value) ->
                plugin.runLater(delay) {
                    plugin.runSchedule {
                        value.forEach {
                            action.invoke(it)
                        }
                    }
                    taskList.remove(this)
                }?.let { taskList.add(it) }
            }
        }
    }
}
