package com.github.syari.ss.plugins.mobarena.hook

import com.github.syari.ss.plugins.core.code.EventRegister
import com.github.syari.ss.plugins.core.code.ListenerFunctions
import com.github.syari.ss.plugins.mobarena.hook.condition.InArenaCondition
import com.github.syari.ss.plugins.mobarena.hook.mechanic.ArenaAnnounce
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent

object MythicMobsRegister : EventRegister {
    override fun ListenerFunctions.events() {
        event<MythicConditionLoadEvent> { e ->
            when (e.conditionName.toLowerCase()) {
                InArenaCondition.NAME -> InArenaCondition(e.config.line)
                else -> return@event
            }.let { e.register(it) }
        }
        event<MythicMechanicLoadEvent> { e ->
            when (e.mechanicName.toLowerCase()) {
                ArenaAnnounce.NAME -> ArenaAnnounce(e.container.configLine, e.config)
                else -> return@event
            }.let { e.register(it) }
        }
    }
}
