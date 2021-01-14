package com.github.syari.ss.plugins.core.scheduler

import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runLater
import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runRepeatTimes
import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runTimer
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class CustomRunnable internal constructor(
    private val plugin: JavaPlugin,
    private val action: CustomTask.() -> Unit
) : CustomTask {
    private val isRunning
        get() = alreadyInit && !task.isCancelled

    private var alreadyInit = false

    /**
     * @see BukkitTask
     */
    lateinit var task: BukkitTask

    /**
     * @see BukkitRunnable
     */
    lateinit var runnable: BukkitRunnable

    /**
     * キャンセルされているか取得します
     */
    override val isCanceled get() = alreadyInit && task.isCancelled

    // override val isAsync get() = alreadyInit && task.isSync

    /**
     * 残りリピート回数
     */
    override var repeatRemain = 0

    private var onEndRepeatTask: (() -> Unit)? = null

    private var onCancelTask: (() -> Unit)? = null

    /**
     * @see CreateScheduler.runSchedule
     * @return [CustomTask]?
     */
    fun runSchedule(async: Boolean = false): CustomTask? {
        return runLater(0, async)
    }

    /**
     * @see CreateScheduler.runLater
     * @return [CustomTask]?
     */
    fun runLater(
        delay: Long,
        async: Boolean = false
    ): CustomTask? {
        return runTimer(-1, delay, async)
    }

    /**
     * @see CreateScheduler.runTimer
     * @return [CustomTask]?
     */
    fun runTimer(
        period: Long,
        delay: Long = 0,
        async: Boolean = false
    ): CustomTask? {
        return runRepeatTimes(period, -1, delay, async)
    }

    /**
     * @see CreateScheduler.runRepeatTimes
     * @return [CustomTask]?
     */
    fun runRepeatTimes(
        period: Long,
        times: Int,
        delay: Long = 0,
        async: Boolean = false
    ): CustomTask? {
        return if (isRunning) {
            null
        } else {
            if (0 < times) {
                repeatRemain = times
                runnable = object : BukkitRunnable() {
                    override fun run() {
                        action()
                        if (repeatRemain == 0) {
                            onEndRepeatTask?.invoke()
                            cancel()
                        } else {
                            repeatRemain--
                        }
                    }
                }
            } else {
                repeatRemain = -1
                runnable = object : BukkitRunnable() {
                    override fun run() {
                        action()
                    }
                }
            }
            this.task = if (async) {
                runnable.runTaskTimerAsynchronously(plugin, delay, period)
            } else {
                runnable.runTaskTimer(plugin, delay, period)
            }
            alreadyInit = true
            this
        }
    }

    /**
     * キャンセルします
     * @return [Boolean]
     */
    override fun cancel(): Boolean {
        return if (isRunning) {
            task.cancel()
            onCancelTask?.invoke()
            if (repeatRemain == 0) {
                onEndRepeatTask?.invoke()
            }
            true
        } else {
            false
        }
    }

    /**
     * リピートが終了したら実行されます
     * @param action 終了時に実行する処理
     * @return [CustomTask]
     */
    override fun onEndRepeat(action: () -> Unit): CustomTask {
        onEndRepeatTask = action
        return this
    }

    /**
     * キャンセルされたら実行されます
     * @param action キャンセル時に実行する処理
     * @return [CustomTask]
     */
    override fun onCancel(action: () -> Unit): CustomTask {
        onCancelTask = action
        return this
    }
}
