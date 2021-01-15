package com.github.syari.ss.plugins.core.scheduler

interface CustomTask {
    val isCanceled: Boolean

    val isSync: Boolean

    val repeatRemain: Int

    fun cancel(): Boolean

    fun onEndRepeat(action: () -> Unit): CustomTask?

    fun onCancel(action: () -> Unit): CustomTask?
}
