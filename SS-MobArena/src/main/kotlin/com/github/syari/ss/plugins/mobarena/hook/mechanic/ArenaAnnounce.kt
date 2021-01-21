package com.github.syari.ss.plugins.mobarena.hook.mechanic

import com.github.syari.ss.plugins.mobarena.MobArenaManager
import com.github.syari.ss.plugins.mobarena.MobArenaManager.arenaPlayer
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill
import io.lumine.xikage.mythicmobs.skills.SkillMechanic
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import org.bukkit.entity.Player

class ArenaAnnounce(skill: String, mlc: MythicLineConfig) : SkillMechanic(skill, mlc), ITargetedEntitySkill {
    companion object {
        const val NAME = "arenaannounce"
    }

    private val message = mlc.getString(arrayOf("message", "msg", "m"))

    override fun castAtEntity(data: SkillMetadata, target: AbstractEntity): Boolean {
        val entity = target.bukkitEntity
        if (entity is Player) {
            entity.arenaPlayer?.arena
        } else {
            MobArenaManager.getArena(entity)
        }?.announce(message)
        return true
    }
}
