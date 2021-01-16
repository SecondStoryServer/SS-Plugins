@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.event

import org.bukkit.event.Cancellable

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
