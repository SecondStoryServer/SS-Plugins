package com.github.syari.ss.plugins.mobarena.wave

import com.github.syari.ss.plugins.core.bossBar.CustomBossBar.Companion.bossBar
import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runTimer
import com.github.syari.ss.plugins.mobarena.Main.Companion.plugin
import com.github.syari.ss.plugins.mobarena.arena.MobArena
import com.github.syari.ss.plugins.mobarena.wave.boss.MobArenaBoss
import com.github.syari.ss.plugins.mobarena.wave.mob.MobArenaMob
import org.bukkit.attribute.Attribute
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.LivingEntity

class MobArenaWave(
    private val arena: MobArena,
    val waveRange: IntRange,
    private val mobAmount: Int,
    val stop: Boolean,
    mobs: List<MobArenaMob>,
    private val boss: MobArenaBoss?,
    val upgrade: List<CustomItemStack>
) {
    private val withPriorityList: List<Pair<IntRange, MobArenaMob>>
    private val maxPriority: Int

    init {
        var priority = 0
        withPriorityList = mobs.map {
            val lastPriority = priority
            priority += it.priority
            lastPriority..priority to it
        }
        maxPriority = priority
    }

    fun spawn() {
        for (i in 0 until mobAmount) {
            val mob = withPriorityList.firstOrNull { (0..maxPriority).random() in it.first }?.second ?: continue
            val entity = mob.spawn(arena.mobSpawn) ?: continue
            arena.mobs.add(entity.uniqueId)
        }
        if (boss != null) {
            val entity = boss.spawn(arena.mobSpawn) ?: return
            arena.mobs.add(entity.uniqueId)
            if (entity is LivingEntity) {
                val bar = bossBar(entity.customName ?: "null", BarColor.RED, BarStyle.SOLID)
                arena.players.forEach { bar.addPlayer(it.player) }
                plugin.runTimer(20) {
                    if (entity.isDead) {
                        bar.delete()
                        cancel()
                    } else {
                        bar.progress = entity.health / entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue
                    }
                }
            }
        }
    }
}
