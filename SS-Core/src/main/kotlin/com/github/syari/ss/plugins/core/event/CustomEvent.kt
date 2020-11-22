package com.github.syari.ss.plugins.core.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Kotlinで自作イベントを作成する場合の基底クラス
 */
open class CustomEvent: Event() {
    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }
}