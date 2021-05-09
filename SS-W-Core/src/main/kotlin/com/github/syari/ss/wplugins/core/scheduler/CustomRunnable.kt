package com.github.syari.ss.wplugins.core.scheduler

import com.github.syari.ss.wplugins.core.scheduler.CreateScheduler.runLater
import com.github.syari.ss.wplugins.core.scheduler.CreateScheduler.runRepeatTimes
import com.github.syari.ss.wplugins.core.scheduler.CreateScheduler.runTimer
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.api.scheduler.ScheduledTask
import java.util.concurrent.TimeUnit

class CustomRunnable internal constructor(
    private val plugin: Plugin,
    private val run: CustomTask.() -> Unit
) : CustomTask {
    private val isRunning
        get() = alreadyInit

    private var alreadyInit = false

    /**
     * @see ScheduledTask
     */
    lateinit var task: ScheduledTask

    /**
     * @see Runnable
     */
    lateinit var runnable: Runnable

    /**
     * キャンセルされているか取得します
     */
    override val isCanceled get() = alreadyInit

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
    fun runSchedule(): CustomTask? {
        return runLater(0)
    }

    /**
     * @see CreateScheduler.runLater
     * @return [CustomTask]?
     */
    fun runLater(
        delay: Long
    ): CustomTask? {
        return runTimer(-1, delay)
    }

    /**
     * @see CreateScheduler.runTimer
     * @return [CustomTask]?
     */
    fun runTimer(
        period: Long,
        delay: Long = 0
    ): CustomTask? {
        return runRepeatTimes(period, -1, delay)
    }

    /**
     * @see CreateScheduler.runRepeatTimes
     * @return [CustomTask]?
     */
    fun runRepeatTimes(
        period: Long,
        times: Int,
        delay: Long = 0
    ): CustomTask? {
        return if (isRunning) {
            null
        } else {
            if (0 < times) {
                repeatRemain = times
                runnable = Runnable {
                    run.invoke(this@CustomRunnable)
                    if (repeatRemain == 0) {
                        onEndRepeatTask?.invoke()
                        cancel()
                    } else {
                        repeatRemain--
                    }
                }
            } else {
                repeatRemain = -1
                runnable = Runnable { run.invoke(this@CustomRunnable) }
            }
            this.task = plugin.proxy.scheduler.schedule(plugin, runnable, delay, period, TimeUnit.MILLISECONDS)
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
     * @param run 終了時に実行する処理
     * @return [CustomTask]
     */
    override fun onEndRepeat(run: () -> Unit): CustomTask {
        onEndRepeatTask = run
        return this
    }

    /**
     * キャンセルされたら実行されます
     * @param run キャンセル時に実行する処理
     * @return [CustomTask]
     */
    override fun onCancel(run: () -> Unit): CustomTask {
        onCancelTask = run
        return this
    }
}
