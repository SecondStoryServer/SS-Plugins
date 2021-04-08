package com.github.syari.ss.plugins.mobarena.hook

import com.github.syari.spigot.api.event.events
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.mobarena.Main.Companion.plugin
import com.github.syari.ss.plugins.mobarena.hook.condition.InArenaCondition
import com.github.syari.ss.plugins.mobarena.hook.mechanic.ArenaAnnounce
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent

object MythicMobsRegister : OnEnable {
    override fun onEnable() {
        plugin.events {
            event<MythicConditionLoadEvent> {
                when (it.conditionName.toLowerCase()) {
                    InArenaCondition.NAME -> InArenaCondition(it.config.line)
                    else -> return@event
                }.run { it.register(this) }
            }
            event<MythicMechanicLoadEvent> {
                when (it.mechanicName.toLowerCase()) {
                    ArenaAnnounce.NAME -> ArenaAnnounce(it.container.configLine, it.config)
                    else -> return@event
                }.run { it.register(this) }
            }
        }
    }
}
