package com.github.syari.ss.plugins.mobarena.hook.condition

import com.github.syari.ss.plugins.mobarena.MobArenaManager.getArenaInPlay
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter
import io.lumine.xikage.mythicmobs.skills.SkillCondition
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition

class InArenaCondition(line: String) : SkillCondition(line), ILocationCondition {
    companion object {
        const val NAME = "inarena"
    }

    override fun check(location: AbstractLocation): Boolean {
        return getArenaInPlay(BukkitAdapter.adapt(location)) != null
    }
}
