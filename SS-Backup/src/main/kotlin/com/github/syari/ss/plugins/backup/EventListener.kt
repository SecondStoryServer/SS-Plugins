package com.github.syari.ss.plugins.backup

import com.github.syari.spigot.api.event.events
import com.github.syari.ss.plugins.backup.Main.Companion.plugin
import com.github.syari.ss.plugins.backup.event.PostBackupEvent
import com.github.syari.ss.plugins.backup.event.PreBackupEvent
import com.github.syari.ss.plugins.core.code.OnEnable

object EventListener : OnEnable {
    override fun onEnable() {
        plugin.events {
            event<PreBackupEvent> { e ->
                e.groups.forEach {
                    it.settings.runPreCommand()
                }
            }
            event<PostBackupEvent> { e ->
                e.groups.forEach {
                    it.settings.runPostCommand()
                }
            }
        }
    }
}
