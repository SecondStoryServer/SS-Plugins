package com.github.syari.ss.plugins.backup

import com.github.syari.ss.plugins.backup.event.PostBackupEvent
import com.github.syari.ss.plugins.backup.event.PreBackupEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object EventListener: Listener {
    @EventHandler
    fun on(e: PreBackupEvent) {
        e.groups.forEach {
            it.settings.runPreCommand()
        }
    }

    @EventHandler
    fun on(e: PostBackupEvent) {
        e.groups.forEach {
            it.settings.runPostCommand()
        }
    }
}