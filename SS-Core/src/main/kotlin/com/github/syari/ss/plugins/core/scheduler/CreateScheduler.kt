package com.github.syari.ss.plugins.core.scheduler

import org.bukkit.plugin.java.JavaPlugin

object CreateScheduler {
    /**
     * @param action 実行する処理
     * @return [CustomRunnable]
     */
    fun JavaPlugin.schedule(
        action: CustomTask.() -> Unit
    ): CustomRunnable {
        return CustomRunnable(this, action)
    }

    /**
     * @param async 非同期か default: false
     * @param action 実行する処理
     * @return [CustomTask]?
     */
    fun JavaPlugin.runSchedule(
        async: Boolean = false,
        action: CustomTask.() -> Unit
    ): CustomTask? {
        return schedule(action).runSchedule(async)
    }

    /**
     * @param delay 遅らせる時間 tick
     * @param async 非同期か default: false
     * @param action 遅らせて実行する処理
     * @return [CustomTask]?
     */
    fun JavaPlugin.runLater(
        delay: Long,
        async: Boolean = false,
        action: CustomTask.() -> Unit
    ): CustomTask? {
        return schedule(action).runLater(delay, async)
    }

    /**
     * @param period 繰り返し間隔 tick
     * @param delay 遅らせる時間 tick default: 0
     * @param async 非同期か default: false
     * @param action 繰り返し実行する処理
     * @return [CustomTask]?
     */
    fun JavaPlugin.runTimer(
        period: Long,
        delay: Long = 0,
        async: Boolean = false,
        action: CustomTask.() -> Unit
    ): CustomTask? {
        return schedule(action).runTimer(period, delay, async)
    }

    /**
     * @param period 繰り返し間隔 tick
     * @param times 繰り返し回数
     * @param delay 遅らせる時間 tick default: 0
     * @param async 非同期か default: false
     * @param action 繰り返し実行する処理
     * @return [CustomTask]?
     */
    fun JavaPlugin.runRepeatTimes(
        period: Long,
        times: Int,
        delay: Long = 0,
        async: Boolean = false,
        action: CustomTask.() -> Unit
    ): CustomTask? {
        return schedule(action).runRepeatTimes(period, times, delay, async)
    }

    /**
     * @param listWithDelay キーを待機時間としたマップ
     * @param action 待機後に実行する処理
     * @return [Set]<[CustomTask]>
     */
    @OptIn(ExperimentalStdlibApi::class)
    fun <T> JavaPlugin.runListWithDelay(
        listWithDelay: Map<Long, Set<T>>,
        action: (T) -> Unit
    ): Set<CustomTask> {
        return buildSet {
            listWithDelay.forEach { (delay, value) ->
                runLater(delay, true) {
                    runSchedule(false) {
                        value.forEach {
                            action(it)
                        }
                    }
                    remove(this)
                }?.let(::add)
            }
        }
    }
}
