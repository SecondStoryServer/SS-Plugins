package com.github.syari.ss.wplugins.accessblocker

import com.github.syari.ss.wplugins.core.message.JsonBuilder.Companion.buildJson
import net.md_5.bungee.api.connection.PendingConnection
import net.md_5.bungee.api.event.LoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import java.util.UUID

object WhiteList : Listener {
    var list = listOf<UUID>()

    var isEnable = true

    private fun canJoin(connection: PendingConnection) = list.contains(connection.uniqueId)

    @EventHandler
    fun on(e: LoginEvent) {
        if (isEnable && canJoin(e.connection).not()) {
            e.setCancelReason(
                buildJson {
                    append("&f&lサーバーはメンテナンス中です")
                }
            )
            e.isCancelled = true
        }
    }
}
