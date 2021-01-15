package com.github.syari.ss.plugins.mobarena.hook

import com.github.syari.ss.plugins.mobarena.hook.condition.InArenaCondition
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent
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
}
