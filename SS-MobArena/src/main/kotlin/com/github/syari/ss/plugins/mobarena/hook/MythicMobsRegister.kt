package com.github.syari.ss.plugins.mobarena.hook

import com.github.syari.ss.plugins.mobarena.hook.condition.InArenaCondition
import com.github.syari.ss.plugins.mobarena.hook.mechanic.ArenaAnnounce
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object MythicMobsRegister : Listener {
    @EventHandler
    fun on(e: MythicConditionLoadEvent) {
        when (e.conditionName.toLowerCase()) {
            InArenaCondition.NAME -> InArenaCondition(e.config.line)
            else -> return
        }.let { e.register(it) }
    }

    @EventHandler
    fun on(e: MythicMechanicLoadEvent) {
        when (e.mechanicName.toLowerCase()) {
            ArenaAnnounce.NAME -> ArenaAnnounce(e.container.configLine, e.config)
            else -> return
        }.let { e.register(it) }
    }
}
