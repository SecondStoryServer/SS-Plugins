package com.github.syari.ss.plugins.core.scheduler

import com.github.syari.ss.plugins.core.Main.Companion.corePlugin
import org.bukkit.plugin.java.JavaPlugin

object CreateScheduler {
    /**
     * @param plugin 実行するプラグイン
     * @param run 実行する処理
     * @return [CustomRunnable]
     */
    fun schedule(
        plugin: JavaPlugin,
        run: CustomTask.() -> Unit
    ): CustomRunnable {
        return CustomRunnable(plugin, run)
    }

    /**
     * @param plugin 実行するプラグイン
     * @param async 非同期か default: false
     * @param run 実行する処理
     * @return [CustomTask]?
     */
    fun run(
        plugin: JavaPlugin,
        async: Boolean = false,
        run: CustomTask.() -> Unit
    ): CustomTask? {
        return schedule(plugin, run).run(async)
    }

    /**
     * @param plugin 実行するプラグイン
     * @param delay 遅らせる時間 tick
     * @param async 非同期か default: false
     * @param run 遅らせて実行する処理
     * @return [CustomTask]?
     */
    fun runLater(
        plugin: JavaPlugin,
        delay: Long,
        async: Boolean = false,
        run: CustomTask.() -> Unit
    ): CustomTask? {
        return schedule(plugin, run).runLater(delay, async)
    }

    /**
     * @param plugin 実行するプラグイン
     * @param period 繰り返し間隔 tick
     * @param delay 遅らせる時間 tick default: 0
     * @param async 非同期か default: false
     * @param run 繰り返し実行する処理
     * @return [CustomTask]?
     */
    fun runTimer(
        plugin: JavaPlugin,
        period: Long,
        delay: Long = 0,
        async: Boolean = false,
        run: CustomTask.() -> Unit
    ): CustomTask? {
        return schedule(plugin, run).runTimer(period, delay, async)
    }

    /**
     * @param plugin 実行するプラグイン
     * @param period 繰り返し間隔 tick
     * @param times 繰り返し回数
     * @param delay 遅らせる時間 tick default: 0
     * @param async 非同期か default: false
     * @param run 繰り返し実行する処理
     * @return [CustomTask]?
     */
    fun runRepeatTimes(
        plugin: JavaPlugin,
        period: Long,
        times: Int,
        delay: Long = 0,
        async: Boolean = false,
        run: CustomTask.() -> Unit
    ): CustomTask? {
        return schedule(plugin, run).runRepeatTimes(period, times, delay, async)
    }

    /**
     * @param listWithDelay キーを待機時間としたマップ
     * @param run 待機後に実行する処理
     * @return [Set]<[CustomTask]>
     */
    fun <T> runListWithDelay(
        listWithDelay: Map<Long, Set<T>>,
        run: (T) -> Unit
    ): Set<CustomTask> {
        return mutableSetOf<CustomTask>().also { taskList ->
            listWithDelay.forEach { (delay, value) ->
                runLater(corePlugin, delay, true) {
                    run(corePlugin, false) {
                        value.forEach {
                            run.invoke(it)
                        }
                    }
                    taskList.remove(this)
                }?.let { taskList.add(it) }
            }
        }
    }
}