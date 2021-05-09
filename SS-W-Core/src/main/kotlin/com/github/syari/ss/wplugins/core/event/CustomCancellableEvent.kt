package com.github.syari.ss.wplugins.core.event

import net.md_5.bungee.api.plugin.Cancellable

/**
 * キャンセル可能なイベント
 */
open class CustomCancellableEvent : CustomEvent(), Cancellable {
    private var cancelled: Boolean = false

    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(cancel: Boolean) {
        cancelled = cancel
    }
}
