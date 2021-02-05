package com.github.syari.ss.plugins.backup

import com.github.syari.ss.plugins.backup.event.PostBackupEvent
import com.github.syari.ss.plugins.backup.event.PreBackupEvent
import com.github.syari.ss.plugins.core.code.EventRegister
import com.github.syari.ss.plugins.core.code.Events

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
