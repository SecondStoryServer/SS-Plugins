package com.github.syari.ss.plugins.ma.mob.condition

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter
import io.lumine.xikage.mythicmobs.skills.SkillCondition
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition
import su.nightexpress.ama.api.ArenaAPI

class InArenaCondition(line: String): SkillCondition(line), ILocationCondition {
    companion object {
        const val NAME = "inarena"
    }

    override fun check(location: AbstractLocation): Boolean {
        return ArenaAPI.getArenaManager().getArenaAtLocation(BukkitAdapter.adapt(location)) != null
    }
}