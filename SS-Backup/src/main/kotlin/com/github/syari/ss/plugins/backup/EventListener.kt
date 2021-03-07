package com.github.syari.ss.plugins.backup

import com.github.syari.spigot.api.event.EventRegister
import com.github.syari.spigot.api.event.Events
import com.github.syari.ss.plugins.backup.event.PostBackupEvent
import com.github.syari.ss.plugins.backup.event.PreBackupEvent

object EventListener : EventRegister {
    override fun Events.register() {
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
