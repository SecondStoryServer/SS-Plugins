package com.github.syari.ss.wplugins.core.scheduler

interface CustomTask {
    val isCanceled: Boolean

    // val isAsync: Boolean

    val repeatRemain: Int

    fun cancel(): Boolean

    fun onEndRepeat(run: () -> Unit): CustomTask?

    fun onCancel(run: () -> Unit): CustomTask?
}
